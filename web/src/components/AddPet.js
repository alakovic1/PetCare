import React from 'react'
import Dropdown from 'react-dropdown';
import axios from "axios";
import { getToken, getUser } from "../utilities/Common";
import {NotificationContainer, NotificationManager} from 'react-notifications';

import '../assets/scss/profile.scss'

class AddPet extends React.Component {
    constructor(props) {
      super(props);

      this.state = {
        name: '',
        description: '',
        location: '',
        age: '',
        categories: [],
        options: [],
        categoryObject: {},
        category: '',
        optionRases: [],
        rases: [],
        raseObject: {},
        rase: '',
        message: '',
        image: null,
        adopted: false,
        selectedFile: null,
        isFilePicked: false,
        userRole: (JSON.parse(getUser()))?.role,
        errors: {}
      };
  
      this.handleChange = this.handleChange.bind(this);
      this.handleCategoryChange = this.handleCategoryChange.bind(this);
      this.handleRaseChange = this.handleRaseChange.bind(this);
      this.handleValidation = this.handleValidation.bind(this);
      this.handleSubmit = this.handleSubmit.bind(this);
      this.uploadImage = this.uploadImage.bind(this);
    }

    componentDidMount() {
      axios.get(
          "http://localhost:8088/pet_category_service_api/categories"
      )
      .then((response) => {
        let categoryName = [];
          for(let i = 0; i < response.data.length; i++) {
            categoryName.push(response.data[i].name)
          }
        this.setState({
          ...this.state,
          categories: response.data,
          options: categoryName,
        });
      })
    }
  
    handleChange(event) {
      const value = event.target.value;
        this.setState({
            ...this.state,
            [event.target.name]: value
        });
    }

    uploadImage = event => {
      if (event.target.files && event.target.files[0]) {
        let img = event.target.files[0];
        this.setState({
          ...this.state,
          image: URL.createObjectURL(img),
          selectedFile: event.target.files[0],
        });
      }
    };

    handleCategoryChange(event) {

      let choosenCategory = {}

      for(let i = 0; i < this.state.categories.length; i++) {
          if (this.state.categories[i].name === event.value)
            choosenCategory = this.state.categories[i];
      }

        this.setState({
            ...this.state,
            category: event.value,
            categoryObject: choosenCategory,
            raseObject: {},
            rase: '',
        });

        axios.get(
            `http://localhost:8088/pet_category_service_api/rases/inCategory?id=${choosenCategory.id}`
        )
        .then((response) => {
          let raseName = [];
            for(let i = 0; i < response.data.length; i++) {
              raseName.push(response.data[i].name)
            }
          this.setState({
            ...this.state,
            rases: response.data,
            optionRases: raseName,
          });
        })

    }

    handleRaseChange(event) {
      const rasa = this.state.rases.find((element) => {
        return element.name === event.value;
      })
      this.setState({
          ...this.state,
          raseObject: rasa
      });
  }

    handleValidation(){
      let errors = {};
      let formIsValid = true;
      
      //name
      if(!this.state.name){
        formIsValid = false;
        errors["name"] = "This field cannot be empty!";
     }

      else if(this.state.name.length < 2 ){
        formIsValid = false;
        errors["name"] = "Names min length is 2!";
      }

     else if(this.state.name.length > 50 ){
        formIsValid = false;
        errors["name"] = "Names max length is 50!";
      }

      //location
      if(!this.state.location){
        formIsValid = false;
        errors["location"] = "This field cannot be empty!";
      }

     //age
     if(!this.state.age){
        formIsValid = false;
        errors["age"] = "This field cannot be empty!";
      }

      else if(this.state.age > 100 ){
        formIsValid = false;
        errors["age"] = "Pet can't be older than 100 years!";
      }

      this.setState({errors: errors});
      return formIsValid;
    }
  
    handleSubmit(event) {
      event.preventDefault();
      let formData = new FormData();  
      formData.append('image', this.state.selectedFile);
      formData.append('message', this.state.message);
      if (this.handleValidation() && this.state.userRole === 'admin') {
        axios.post(`http://localhost:8088/pet_category_service_api/pet?name=${this.state.name}&location=${this.state.location}&description=${this.state.description}&age=${this.state.age}&rase_id=${this.state.raseObject?.id}`,
          formData, 
          {
            headers: {
              Authorization: "Bearer " + getToken(),
            },   
        }).then(res => {
            return NotificationManager.success(res.data.message, '  ', 3000);
        }).catch((error) => {
          return NotificationManager.error('Pet is not added', '  ', 3000);
        });
      }
      else if (this.handleValidation() && this.state.userRole === 'user') {
        axios.post(`http://localhost:8088/adopt_service_api/eurekaa/add-pet-request?location=${this.state.location}&description=${this.state.description}&age=${this.state.age}&rase_id=${this.state.raseObject?.id}&name=${this.state.name}`,
          formData, 
          {
            headers: { 
              Authorization: "Bearer " + getToken(),
            },   
        }).then(res => {
            return NotificationManager.success(res.data.message, '  ', 3000);
        }).catch((error) => {
          return NotificationManager.error('Pet is not added', '  ', 3000);
        });
      }
      else return;
    }
  
    render() {
      return (
        <div>
            <form className={"form"} onSubmit={this.handleSubmit}>
                <div className={"form-elements"}>
                    <input type="text" name="name" value={this.state.name} onChange={this.handleChange} placeholder="Name"/>
                    <span className={"error"}>{this.state.errors["name"]}</span>
                    <br/>
                    <input type="text" name="description" value={this.state.description} onChange={this.handleChange} placeholder="Description"/>
                    <br/>
                    <input type="text" name="location" value={this.state.location} onChange={this.handleChange} placeholder="Location"/>
                    <span className={"error"}>{this.state.errors["location"]}</span>
                    <br/>
                    <input type="number" min="0" max="100" name="age" value={this.state.age} onChange={this.handleChange} placeholder="Age"/>
                    <span className={"error"}>{this.state.errors["age"]}</span>
                    <br/>
                    <div>
                      <img src={this.state.image}/>
                      <input type="file" name="image" onChange={this.uploadImage} />
                    </div>
                    <Dropdown className={"dropdown"} options={this.state.options} name="options" onChange={this.handleCategoryChange} value={this.state.category} placeholder="Select pet category" />
                    <br/>
                    <Dropdown className={"dropdown"} options={this.state.optionRases} name="rase" onChange={this.handleRaseChange} value={this.state.rase} placeholder="Select pet rase" />
                    <br/>
                    <textarea type="text" name="message" value={this.state.message} onChange={this.handleChange} placeholder="Message"/>
                    <span className={"error"}>{this.state.errors["message"]}</span>
                    <br/>
                    <input type="submit" value="Add" className="btn px-6 py-3 bg-red-500 text-white text-center margin-auto mt-8"/>
                </div>
            </form>
        </div>
      );
    }
  }
  
  export default AddPet 