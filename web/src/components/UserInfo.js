import React from 'react'
import axios from "axios";
import { getToken, getUser, logoutUser } from "../utilities/Common";
import {NotificationContainer, NotificationManager} from 'react-notifications';

import 'react-notifications/lib/notifications.css';
import '../assets/scss/login.scss'

class UserInfo extends React.Component {
    constructor(props) {
        super(props);
        
        this.state = {
          name: '',
          surname: '',
          username: '',
          email: '',
          password: '',
          user: JSON.parse(getUser()),
          errors: {}
        };
    
        this.handleChange = this.handleChange.bind(this);
        this.userInfomartion = this.userInfomartion.bind(this);
        this.handleValidation = this.handleValidation.bind(this);
        this.updateUser = this.updateUser.bind(this);
        this.deleteProfile = this.deleteProfile.bind(this)
      }

    handleChange(event) {
      const value = event.target.value;
        this.setState({
            ...this.state,
            [event.target.name]: value
        });
    }

    componentDidMount() {
        axios.get(
          "http://localhost:8088/user_service_api/user/me",
          {
            headers: {
               Authorization: "Bearer " + getToken(),
            },
          }
        )
      .then((response) => {
        this.setState({
          ...this.state,
          name: response.data.name,
          surname: response.data.surname,
          username: response.data.username,
          email: response.data.email
      });
      }).then(() => {
        axios.get(
          "http://localhost:8088/pet_category_service_api/categories"
        )
        .then((categories) => {
        })
      })
    }
    
    userInfomartion(event) {
        const value = event.target.value;
        this.setState({
            ...this.state,
            [event.target.name]: value
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

      else if(this.state.name.length < 3 ){
        formIsValid = false;
        errors["name"] = "Names min length is 3!";
      }

     else if(this.state.name.length > 50 ){
        formIsValid = false;
        errors["name"] = "Names max length is 50!";
      }

      //surname
      if(!this.state.surname){
        formIsValid = false;
        errors["surname"] = "This field cannot be empty!";
      }

      else if(this.state.surname.length < 3 ){
        formIsValid = false;
        errors["surname"] = "Surnames min length is 3!";
      }

      else if(this.state.surname.length > 50 ){
        formIsValid = false;
        errors["surname"] = "Surnames max length is 50!";
      }

      if(!this.state.username){
        formIsValid = false;
        errors["username"] = "This field cannot be empty!";
     }

     else if(this.state.username.length < 4 ){
       formIsValid = false;
       errors["username"] = "Usernames min length is 4!";
    }

     else if(this.state.username.length > 40 ){
       formIsValid = false;
       errors["username"] = "Usernames max length is 40!";
     } 

      this.setState({errors: errors});
      return formIsValid;
    }
    
    updateUser(event) {
      event.preventDefault();
      if (this.handleValidation()) {
        axios.get(
          "http://localhost:8088/user_service_api/user/usernameCheck?username="+this.state.username,
        )
        .then((response) => {
          response.data.available = true;
          if (response.data.available === false) {
            this.setState({
              ...this.state,
              availableUsername: false
            });
            NotificationManager.error("Username is already in use!", '  ', 3000);
          }
          else {
            axios.put('http://localhost:8088/user_service_api/user/update', 
            {
              name: this.state.name,
              surname: this.state.surname,
              email: this.state.email,
              username: this.state.username
            }, 
            {
              headers: {
                Authorization: "Bearer " + getToken(),
              },  
            }).then(res => {
              return NotificationManager.success(res.data.message, '  ', 3000);
            }).catch((error) => {
              return NotificationManager.error(error.response.data.details[0], '  ', 3000);
            }); 
          }
        })
      }
      else return;
    }

    deleteProfile(event) {
      event.preventDefault();
      axios.delete(
        `http://localhost:8088/user_service_api/user/delete`,
        {
          headers: {
             Authorization: "Bearer " + getToken(),
          },
          data: {
            email: this.state.email,
            password: this.state.password
          }
        }).then(res => {
          this.props.history.push("/login");
          return NotificationManager.success(res.data.message, '  ', 3000);
        }).catch((error) => {
          return NotificationManager.error(error.response.data.details[0], '  ', 3000);
        }).catch((error) => {
          logoutUser();
          window.location.reload();
          return NotificationManager.success('Account deleted!', '  ', 3000);
        }); 
    }
    
    render() {
      return (
        <div>
           <form className={"form"} onSubmit={this.updateUser}>
                <div className={"form-elements"}>
                    <input type="text" name="name" value={this.state.name} onChange={this.userInfomartion} placeholder="Name"/>
                    <span className={"error"}>{this.state.errors["name"]}</span>
                    <br/>
                    <input type="text" name="surname" value={this.state.surname} onChange={this.userInfomartion} placeholder="Surname"/>
                    <span className={"error"}>{this.state.errors["surname"]}</span>
                    <br/>
                    <input type="text" name="username" value={this.state.username} onChange={this.userInfomartion} placeholder="Username"/>
                    <br/>
                    <input disabled type="email" name="email" value={this.state.email} onChange={this.userInfomartion} placeholder="Email"/>
                    <br/>
                    <input type="submit" value="Update" className="btn px-6 py-3 bg-red-500 text-white text-center margin-auto mt-8"/>
                </div>
            </form>

            <br/>
            <br/>
            <h3 className="text-center deleteText">If you want to delete your profile first insert your password and then delete</h3>
            <input type="text" name="password" value={this.state.password} onChange={this.handleChange} placeholder="Password" />
            <span className={"error"}>{this.state.errors["password"]}</span>
            <button onClick={this.deleteProfile} className="btn px-6 py-3 bg-red-500 text-white text-center margin-auto mt-8 deleteBtn">DELETE</button>

            <NotificationContainer/>  
        </div>
      );
    }
  }
  
  export default UserInfo 