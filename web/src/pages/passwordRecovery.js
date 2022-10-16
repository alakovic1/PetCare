import React from 'react'
import { Link } from 'react-router-dom'
import Dropdown from 'react-dropdown';
import axios from "axios";
import { getToken, getUser } from "../utilities/Common";
import {NotificationContainer, NotificationManager} from 'react-notifications';

import 'react-notifications/lib/notifications.css';
import '../assets/scss/login.scss'

class PasswordRecovery extends React.Component {
    constructor(props) {
        super(props);
        
        this.state = {
          newPassword: '',
          emailChecked: false,
          email: '',
          question: {},
          answer: '',
          sameAnswer: false,
          errors: {}
        };
        
        this.submitMail = this.submitMail.bind(this);
        this.userInfomartion = this.userInfomartion.bind(this);
        this.changePassword = this.changePassword.bind(this);
        this.checkAnswer = this.checkAnswer.bind(this);
        this.handleValidationAnswer = this.handleValidationAnswer.bind(this);
        this.handleValidationPassword = this.handleValidationPassword.bind(this);
        this.handleDropdownChange = this.handleDropdownChange.bind(this);
      }
    
    userInfomartion(event) {
        event.preventDefault();
        const value = event.target.value;
        this.setState({
            ...this.state,
            [event.target.name]: value
        });
    }

    submitMail(event) {
        event.preventDefault();
        axios.post('http://localhost:8088/user_service_api/recovery/securityquestion', 
            {
              email: this.state.email
            }).then(res => {
                this.setState({
                    ...this.state,
                    question: res.data.question,
                    emailChecked: true,
                }); 
          }).catch((error) => {
            return NotificationManager.error(error.response.data.details[0], '  ', 3000);
        });
    }

    handleDropdownChange(event) {
        event.preventDefault();
        this.setState({
            ...this.state,
            question: event.value
        });
    }

    handleValidationAnswer(){
        let errors = {};
        let formIsValid = true;

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
            errors["email"] = "Email should be valid";
            }
        }

        //answer
        if(!this.state.answer){
            formIsValid = false;
            errors["answer"] = "This field cannot be empty!";
        }
    
        this.setState({errors: errors});
        return formIsValid;
      }

      handleValidationPassword(){
        let errors = {};
        let formIsValid = true;

        //password
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

    checkAnswer(event) {
      event.preventDefault();
      axios.post('http://localhost:8088/user_service_api/recovery/answerQuestion', 
        {
          email: this.state.email,
          answer: {
              text: this.state.answer
            }
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
        if (this.handleValidationPassword()) {
            axios.post('http://localhost:8088/user_service_api/recovery/newPassword', 
            {
              email: this.state.email,
              answer: {
                  text: this.state.answer
              },
              newPassword: this.state.newPassword,
              oldPassword: this.state.oldPassword
            }).then(res => {
              this.setState({
                ...this.state,
                newPassword: '',
              });
              this.props.history.push("/login");
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
            <h2 className="text-center margin-auto">Password recovery</h2>
            <br/>
            <div className={"form-elements"}>
                {!this.state.sameAnswer && !this.state.emailChecked &&
                    <div>
                        <form className={"form"} onSubmit={this.submitMail}>
                            <input type="email" name="email" value={this.state.email} onChange={this.userInfomartion} placeholder="Email"/>
                            <span className={"error"}>{this.state.errors["email"]}</span>
                            <input type="submit" value="Submit" className="btn px-6 py-3 bg-red-500 text-white text-center margin-auto mt-8 mb-5"/>
                        </form>
                    </div>
                }
                {!this.state.sameAnswer && this.state.emailChecked &&
                    <form className={"form"} onSubmit={this.checkAnswer}>
                        <h6>{this.state.question.title}</h6>
                        <br/>
                        <textarea type="text" name="answer" value={this.state.answer} onChange={this.userInfomartion} placeholder="Answer"/>
                        <span className={"error"}>{this.state.errors["answer"]}</span>
                        <input type="submit" value="Check answer" className="btn px-6 py-3 bg-red-500 text-white text-center margin-auto mt-8"/>
                    </form>
                }
                
                {this.state.sameAnswer && this.state.emailChecked &&
                    <div>
                        <form className={"form"} onSubmit={this.changePassword}>
                            <input type="password" name="newPassword" value={this.state.newPassword} onChange={this.userInfomartion} placeholder="New password" />
                            <span className={"error"}>{this.state.errors["newPassword"]}</span>
                            <input type="submit" value="Submit" className="btn px-6 py-3 bg-red-500 text-white text-center margin-auto mt-8 mb-5"/>
                        </form>
                    </div>
                }
            </div> 
            <NotificationContainer/>
        </div>
      );
    }
  }
  
  export default PasswordRecovery 