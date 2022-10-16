import React from 'react'
import { Link } from 'react-router-dom';
import axios from "axios";
import { getToken, getUser, logoutUser } from "../utilities/Common";
import {NotificationContainer, NotificationManager} from 'react-notifications';

import '../assets/scss/comment.scss'

import avatar from "../assets/icon/user.png";

class Reply extends React.Component {
  constructor(props) {
    super(props);
    
    this.state = {
      addComment: false,
      addReply: false,
      commentId: props.commentId,
      replyContent: '',
      replyCommentId: -1,
      errors: {}
    }; 

    this.toggleReply = this.toggleReply.bind(this);
    this.handleValidationReply = this.handleValidationReply.bind(this);
    this.handleReplyChange = this.handleReplyChange.bind(this);
    this.submitReply = this.submitReply.bind(this);

  }

  toggleReply(clickedId) {
    if(this.state.replyCommentId === -1) {
      this.setState({
        ...this.state,
        addReply: !this.state.addReply,
        replyCommentId: clickedId
      });
    }
    else if (this.state.replyCommentId === clickedId) {
      this.setState({
        ...this.state,
        addReply: !this.state.addReply,
        replyCommentId: -1
      });
    }
    else {
      this.setState({
        ...this.state,
        replyCommentId: clickedId
      });
    }
  }

  handleReplyChange(event) {
    const value = event.target.value;
    this.setState({
        ...this.state,
        [event.target.name]: value
    });
    event.preventDefault();
  } 

  handleValidationReply(){
    let errors = {};
    let formIsValid = true;

    //replyContent
    if(!this.state.replyContent){
      formIsValid = false;
      errors["replyContent"] = "Content can't be blank!";
   }

    else if(this.state.replyContent.length < 2 || this.state.replyContent.length > 1000 ){
      formIsValid = false;
      errors["replyContent"] = "Content must be between 2 and 1000 characters!";
    }

    this.setState({errors: errors});
    return formIsValid;
  }

  submitReply(event) {
    event.preventDefault();
    if (this.handleValidationReply()) {
        axios.post(`http://localhost:8088/comment_service_api/reply/${this.state.commentId}`, 
        {
          content: this.state.replyContent
        }, 
        {
          headers: {
            Authorization: "Bearer " + getToken(),
          },  
        }).then(res => {
          window.location.reload();
          return NotificationManager.success(res.data.message, '', 3000);
        }).catch((error) => {
          return NotificationManager.error(error.response.data.details[0], '  ', 3000);
        }); 
    }
    else return;
  }

    render () {
      return(
        <div className="reply-area">
            <form className={"form"} onSubmit={this.submitReply}>
              <textarea type="text" name="replyContent" value={this.state.replyContent} onChange={this.handleReplyChange} placeholder="Content"/>
              <span className={"error"}>{this.state.errors["replyContent"]}</span>
              <br/>
              <input type="submit" value="Submit" className="btn px-6 py-3 bg-red-500 text-white text-center margin-auto mt-8 buttons"/>
            </form>

          <NotificationContainer/>  
        </div>        
      );
    }
}

export default Reply
