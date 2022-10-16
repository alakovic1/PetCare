import React from 'react'
import { Link } from 'react-router-dom'
import Dropdown from 'react-dropdown';
import axios from "axios";
import {NotificationContainer, NotificationManager} from 'react-notifications';

import 'react-notifications/lib/notifications.css';
import '../assets/scss/login.scss'

class Register extends React.Component {
    constructor(props) {
      super(props);
      
      this.options = [];
      this.defaultOption = null;
      this.state = {
        name: '',
        surname: '',
        username: '',
        email: '',
        password: '',
        questions: [],
        options: [],
        question: {},
        answer: '',
        availableUsername: true,
        availableEmail: true,
        errors: {}
      };
  
      this.handleChange = this.handleChange.bind(this);
      this.handleDropdownChange = this.handleDropdownChange.bind(this);
      this.handleValidation = this.handleValidation.bind(this);
      this.handleSubmit = this.handleSubmit.bind(this);
    }

    componentDidMount() {
      axios.get(
        "http://localhost:8088/user_service_api/questions",
      )
      .then((response) => {
          if(response.data) {
            let questionTitle = [];
          for(let i = 0; i < response.data.length; i++) {
            questionTitle.push(response.data[i].title)
          }
          this.setState({
            ...this.state,
            questions: response.data,
            options: questionTitle,
          });
        }
      })
    }
  
    handleChange(event) {
        const value = event.target.value;
        this.setState({
            ...this.state,
            [event.target.name]: value
        });
    }

    handleDropdownChange(event) {
      const que = this.state.questions.find((element) => {
        return element.title === event.value;
      })

      this.setState({
          ...this.state,
          question: que
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

     //email
     if(!this.state.email){
        formIsValid = false;
        errors["email"] = "This field cannot be empty!";
      }

      else if(this.state.email.length > 100 ){
        formIsValid = false;
        errors["email"] = "Emails max length is 100!";
      }

      else {               
        let lastAtPos = this.state.email.lastIndexOf('@');
        let lastDotPos = this.state.email.lastIndexOf('.');

        if (!(lastAtPos < lastDotPos && lastAtPos > 0 && this.state.email.indexOf('@@') === -1 && lastDotPos > 2 && (this.state.email.length - lastDotPos) > 2)) {
          formIsValid = false;
          errors["email"] = "Email should be valid!";
        }

        axios.get(
          "http://localhost:8088/user_service_api/user/emailCheck?email="+this.state.email,
        )
        .then((response) => {
          if (response.data.available === false) {
            this.setState({
              ...this.state,
              availableEmail: false
            });
          }
        }).then(() => {
          if(this.state.availableEmail === false) {
            formIsValid = false;
          }
        })
      }
      
      //username
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

      else {
        axios.get(
          "http://localhost:8088/user_service_api/user/usernameCheck?username="+this.state.username,
        )
        .then((response) => {
          if (response.data.available === false) {
            this.setState({
              ...this.state,
              availableUsername: false
            });
          }
        }).then(() => {
          if(this.state.availableUsername === false) {
            formIsValid = false;
          }
        })
      }

      //password
      if(!this.state.password){
        formIsValid = false;
        errors["password"] = "This field cannot be empty!";
     }

      else if(this.state.password.length < 6 ){
        formIsValid = false;
        errors["password"] = "Passwords min length is 6!";
      }

      //question
      if(!this.state.question.title){
        formIsValid = false;
        errors["question"] = "This field cannot be empty!";
     }

      //answer
      if(!this.state.answer){
        formIsValid = false;
        errors["answer"] = "This field cannot be empty!";
     }
  
      this.setState({errors: errors});
      return formIsValid;
    }
  
    handleSubmit(event) {
      event.preventDefault();
      if (this.handleValidation()) {
        axios.post('http://localhost:8088/user_service_api/api/auth/register/' + this.state.question.id, {
            name: this.state.name,
            surname: this.state.surname,
            email: this.state.email,
            username: this.state.username,
            password: this.state.password,
            answer: {
                text: this.state.answer
            }
        }).then(res => {
            this.props.history.push("/login");
            return NotificationManager.success('Successful registration', '  ', 3000);
        }).catch((error) => {
          return NotificationManager.error(error.response.data.details[0], '  ', 3000);
        });
      }
      else return;
    }
  
    render() {
      return (
        <div>
            <h2 className="text-center margin-auto">Register</h2>
            <form className={"form"} onSubmit={this.handleSubmit}>
                <div className={"form-elements"}>
                    <input type="text" name="name" value={this.state.name} onChange={this.handleChange} placeholder="Name"/>
                    <span className={"error"}>{this.state.errors["name"]}</span>
                    <br/>
                    <input type="text" name="surname" value={this.state.surname} onChange={this.handleChange} placeholder="Surname"/>
                    <span className={"error"}>{this.state.errors["surname"]}</span>
                    <br/>
                    <input type="text" name="username" value={this.state.username} onChange={this.handleChange} placeholder="Username"/>
                    <span className={"error"}>{this.state.errors["username"]}</span>
                    <br/>
                    <input type="email" name="email" value={this.state.email} onChange={this.handleChange} placeholder="Email"/>
                    <span className={"error"}>{this.state.errors["email"]}</span>
                    <br/>
                    <input type="password" name="password" value={this.state.password} onChange={this.handleChange} placeholder="Password" />
                    <span className={"error"}>{this.state.errors["password"]}</span>
                    <br/>
                    <Dropdown className={"dropdown"} options={this.state.options} name="question" onChange={this.handleDropdownChange} value={this.state.question.title} placeholder="Choose security question" />
                    <span className={"error"}>{this.state.errors["question"]}</span>
                    <br/>
                    <textarea type="text" name="answer" value={this.state.answer} onChange={this.handleChange} placeholder="Answer"/>
                    <span className={"error"}>{this.state.errors["answer"]}</span>
                    <br/>
                    <input type="submit" value="Register" className="btn px-6 py-3 bg-red-500 text-white text-center margin-auto mt-8"/>
                </div>
            </form>
            <div className="text-center margin-auto pt-4 pb-10">
                <h6>You already have an account?  
                <Link to={"/login"}>
                    <button className="text-red-500 btn-link">    
                    Login
                    </button>
                </Link>
                </h6>
            </div>

          <NotificationContainer/>  
        </div>
      );
    }
  }
  
  export default Register 