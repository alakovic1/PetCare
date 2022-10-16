import React from 'react'
import { Link } from 'react-router-dom'
import Dropdown from 'react-dropdown';
import axios from "axios";
import { getToken, getUser } from "../utilities/Common";
import {NotificationContainer, NotificationManager} from 'react-notifications';

import 'react-notifications/lib/notifications.css';
import '../assets/scss/login.scss'

class ChangePassword extends React.Component {
    constructor(props) {
        super(props);
        
        this.state = {
          email: '',
          oldPassword: '',
          newPassword: '',
          question: {},
          answer: '',
          sameAnswer: false,
          errors: {}
        };
    
        this.userInfomartion = this.userInfomartion.bind(this);
        this.checkAnswer = this.checkAnswer.bind(this);
        this.handleValidationAnswer = this.handleValidationAnswer.bind(this);
        this.handleValidation = this.handleValidation.bind(this);
        this.changePassword = this.changePassword.bind(this);
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
            oldPassword: response.data.password,
            email: response.data.email
        });
          axios.post('http://localhost:8088/user_service_api/change/securityquestion', 
            {
              email: this.state.email
            }, 
            { 
              headers: {
                Authorization: "Bearer " + getToken(),
              },  
            }).then(res => {
              this.setState({
                ...this.state,
                question: res.data.question
              });
          }).catch(error => {
          });
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

      //password
      if(!this.state.oldPassword){
        formIsValid = false;
        errors["password"] = "This field cannot be empty!";
     }

      else if(this.state.oldPassword.length < 6 ){
        formIsValid = false;
        errors["password"] = "Passwords min length is 6!";
      }

      if(!this.state.newPassword){
        formIsValid = false;
        errors["newPassword"] = "This field cannot be empty!";
     }

      else if(this.state.newPassword.length < 6 ){
        formIsValid = false;
        errors["newPassword"] = "New passwords min length is 6!";
      }
  
      this.setState({errors: errors});
      return formIsValid;
    }

    handleValidationAnswer(){
      let errors = {};
      let formIsValid = true;

      //answer
      if(!this.state.answer){
          formIsValid = false;
          errors["answer"] = "This field cannot be empty!";
      }
  
      this.setState({errors: errors});
      return formIsValid;
    }

    checkAnswer(event) {
      event.preventDefault();
      axios.post('http://localhost:8088/user_service_api/change/answerQuestion', 
        {
          email: this.state.email,
          answer: {
              text: this.state.answer
            }
        }, 
        { 
          headers: {
            Authorization: "Bearer " + getToken(),
          },  
        }).then(res => {
          this.setState({
            ...this.state,
            sameAnswer: true
        });
      }).catch((error) => {
        return NotificationManager.error(error.response.data.details[0], '  ', 3000);
      });
    }
    
    changePassword(event) {
      event.preventDefault();
      if (this.handleValidation()) {
        axios.post('http://localhost:8088/user_service_api/change/newPassword', 
          {
            email: this.state.email,
            answer: {
                text: this.state.answer
            },
            newPassword: this.state.newPassword,
            oldPassword: this.state.oldPassword
          }, 
          { 
            headers: {
              Authorization: "Bearer " + getToken(),
            },  
          }).then(res => {
            this.setState({
              ...this.state,
              oldPassword: '',
              newPassword: '',
            });
            return NotificationManager.success("Password changed", '  ', 3000);
          }).catch(error => {
            return NotificationManager.error(error.response.data.details[0], '  ', 3000);
        });
      }
      else return;
    }
    
    render() {
      return (
        <div>
          <div className={"form-elements"}>
                {!this.state.sameAnswer && 
                    <form className={"form"} onSubmit={this.checkAnswer}>
                        <h6>{this.state.question.title}</h6>
                        <br/>
                        <textarea type="text" name="answer" value={this.state.answer} onChange={this.userInfomartion} placeholder="Answer"/>
                        <span className={"error"}>{this.state.errors["answer"]}</span>
                        <br/>
                        <input type="submit" value="Check answer" className="btn px-6 py-3 bg-red-500 text-white text-center margin-auto mt-8"/>
                    </form>
                }
                
                {this.state.sameAnswer && 
                    <div>
                        <form className={"form"} onSubmit={this.changePassword}>
                            <input type="password" name="oldPassword" value={this.state.oldPassword} onChange={this.userInfomartion} placeholder="Old password" />
                            <span className={"error"}>{this.state.errors["password"]}</span>
                            <br/>
                            <input type="password" name="newPassword" value={this.state.newPassword} onChange={this.userInfomartion} placeholder="New password" />
                            <span className={"error"}>{this.state.errors["newPassword"]}</span>
                            <br/>
                            <input type="submit" value="Submit" className="btn px-6 py-3 bg-red-500 text-white text-center margin-auto mt-8"/>
                      </form>
                    </div>
                }
            </div>
            <NotificationContainer/> 
        </div>
      );
    }
  }
  
  export default ChangePassword 