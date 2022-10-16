import React from 'react'
import { Link } from 'react-router-dom'
import Reply from './Reply';
import axios from "axios";
import { getToken, getUser, logoutUser } from "../utilities/Common";
import {NotificationContainer, NotificationManager} from 'react-notifications';

import '../assets/scss/comment.scss'

import avatar from "../assets/icon/user.png";

class Comment extends React.Component {
  constructor(props) {
    super(props);
    
    this.state = {
      token: getToken(),
      user: JSON.parse(getUser()),
      addComment: false,
      addReply: false,
      commentTitle: '',
      commentContent: '',
      replyContent: '',
      replyCommentId: -1,
      petId: props.id,
      updateComment: false,
      category: props.category,
      updateTitle: '',
      updateContent: '',
      updateCommentId: -1,
      updateReply: false,
      updateReplyContent: '',
      updateReplyId: -1,
      comments: [],
      reply: [],
      errors: {}
    };

    this.toggleComment = this.toggleComment.bind(this);
    this.handleCommentChange = this.handleCommentChange.bind(this);
    this.handleValidationComment = this.handleValidationComment.bind(this);
    this.submitComment = this.submitComment.bind(this);
    this.toggleReply = this.toggleReply.bind(this);
    this.updateCommentFunction = this.updateCommentFunction.bind(this);
    this.toggleUpdateComment = this.toggleUpdateComment.bind(this);
    this.toggleUpdateReply = this.toggleUpdateReply.bind(this);
    this.updateReplyFunction = this.updateReplyFunction.bind(this);
    this.handleValidationReply  = this.handleValidationReply.bind(this);
    this.deleteComment = this.deleteComment.bind(this);
    this.deleteReply = this.deleteReply.bind(this);
  }

  componentDidMount() {
    //1 - category, 2 - pet
    axios.get(
      `http://localhost:8088/comment_service_api/comment/category/${this.state.category}/${this.state.petId}`
    )
    .then((response) => {
      this.setState({
        ...this.state,
        comments: response.data
      });
    })
    .then(() => {
      for (let i = 0; i < this.state.comments.length; i++) {
        axios.get(
          `http://localhost:8088/comment_service_api/reply/comment/${this.state.comments[i].id}`
        ).then((response) => {
          this.setState({
            ...this.state,
            reply: response.data
          });
        })
      }
    })
  }

  toggleComment(event) {
    this.setState({
        ...this.state,
        addComment: !this.state.addComment
    });
  }
  
  handleCommentChange(event) {
    const value = event.target.value;
    this.setState({
        ...this.state,
        [event.target.name]: value
    });
  }

  handleValidationComment(post){
    let errors = {};
    let formIsValid = true;

      if(post === "add") {
        //commentTitle
        if(!this.state.commentTitle){
          formIsValid = false;
          errors["commentTitle"] = "Title can't be blank!";
        }
    
        else if(this.state.commentTitle.length < 2 || this.state.commentTitle.length > 100 ){
          formIsValid = false;
          errors["commentTitle"] = "Title must be between 2 and 100 characters!";
        }
    
        //commentContent
        if(!this.state.commentContent){
          formIsValid = false;
          errors["commentContent"] = "Content can't be blank!";
        }
    
        else if(this.state.commentContent.length < 2 || this.state.commentContent.length > 1000 ){
          formIsValid = false;
          errors["commentContent"] = "Content must be between 2 and 1000 characters!";
        }
      } else {
        //commentTitle
        if(!this.state.updateTitle){
          formIsValid = false;
          errors["updateTitle"] = "Title can't be blank!";
        }
    
        else if(this.state.updateTitle.length < 2 || this.state.updateTitle.length > 100 ){
          formIsValid = false;
          errors["updateTitle"] = "Title must be between 2 and 100 characters!";
        }
    
        //commentContent
        if(!this.state.updateContent){
          formIsValid = false;
          errors["updateContent"] = "Content can't be blank!";
        }
    
        else if(this.state.updateContent.length < 2 || this.state.updateContent.length > 1000 ){
          formIsValid = false;
          errors["updateContent"] = "Content must be between 2 and 1000 characters!";
        }
    }
    
    this.setState({errors: errors});
    return formIsValid;
  }

  submitComment(event) {
    event.preventDefault();
    if (this.handleValidationComment("add")) {
        axios.post(`http://localhost:8088/comment_service_api/comment/${this.state.category}`
        , 
        {
          categoryID: this.state.petId,
          title: this.state.commentTitle,
          content: this.state.commentContent
        }, 
        {
          headers: {
            Authorization: "Bearer " + this.state.token,
          },  
        }).then(res => {
          axios.get(
            `http://localhost:8088/comment_service_api/comment/category/${this.state.category}/${this.state.petId}`
          )
          .then((response) => {
              this.setState({
                ...this.state,
                comments: response.data
              }); 
              this.setState({
                ...this.state,
                commentTitle: '',
                commentContent: '',
                addComment: false,
            });
          })
          return NotificationManager.success(res.data.message, '  ', 3000);
        }).catch((error) => {
          return NotificationManager.error(error.response.data.details[0], '  ', 3000);
        });
      }
    else return;
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

  toggleUpdateComment(comment) {
    this.setState({
      ...this.state,
      updateComment: !this.state.updateComment,
      updateTitle: comment.title,
      updateContent: comment.content,
      updateCommentId: comment.id
    });
  }

  updateCommentFunction(event) {
    event.preventDefault();
    if (this.handleValidationComment("update")) {
        axios.put(`http://localhost:8088/comment_service_api/comment/${this.state.updateCommentId}`
        , 
        {
          title: this.state.updateTitle,
          content: this.state.updateContent
        }, 
        {
          headers: {
            Authorization: "Bearer " + this.state.token,
          },  
        }).then(res => {
          axios.get(
            `http://localhost:8088/comment_service_api/comment/category/${this.state.category}/${this.state.petId}`
          )
          .then((response) => {
              this.setState({
                ...this.state,
                comments: response.data
              }); 
              this.setState({
                ...this.state,
                updateTitle: '',
                updateContent: '',
                updateComment: false,
                updateCommentId: -1
            });
          })
          return NotificationManager.success(res.data.message, '  ', 3000);
        }).catch((error) => {
          return NotificationManager.error(error.response.data.details[0], '  ', 3000);
        });
      }
    else return;
  }

  toggleUpdateReply(reply) {
    this.setState({
      ...this.state,
      updateReply: !this.state.updateComment,
      updateReplyContent: reply.content,
      updateReplyId: reply.id
    });
  }

  handleValidationReply(){
    let errors = {};
    let formIsValid = true;

    //replyContent
    if(!this.state.updateReplyContent){
      formIsValid = false;
      errors["updateReplyContent"] = "Content can't be blank!";
   }

    else if(this.state.updateReplyContent.length < 2 || this.state.updateReplyContent.length > 1000 ){
      formIsValid = false;
      errors["updateReplyContent"] = "Content must be between 2 and 1000 characters!";
    }

    this.setState({errors: errors});
    return formIsValid;
  }

  updateReplyFunction(event) {
    event.preventDefault();
    if (this.handleValidationReply()) {
        axios.put(`http://localhost:8088/comment_service_api/reply/${this.state.updateReplyId}`
        , 
        {
          content: this.state.updateReplyContent
        }, 
        {
          headers: {
            Authorization: "Bearer " + this.state.token,
          },  
        }).then(res => {
          axios.get(
            `http://localhost:8088/comment_service_api/comment/category/${this.state.category}/${this.state.petId}`
          )
          .then((response) => {
              this.setState({
                ...this.state,
                comments: response.data
              }); 
              this.setState({
                ...this.state,
                updateReplyContent: '',
                updateReply: false,
                updateReplyId: -1
            });
          })
          return NotificationManager.success(res.data.message, '  ', 3000);
        }).then(() => {
          for (let i = 0; i < this.state.comments.length; i++) {
            axios.get(
              `http://localhost:8088/comment_service_api/reply/comment/${this.state.comments[i].id}`
            ).then((response) => {
              this.setState({
                ...this.state,
                reply: response.data
              });
            })
          }
        }).catch((error) => {
          return NotificationManager.error(error?.response?.data?.details[0], '  ', 3000);
        });
      }
    else return;
  }

  deleteComment(comment) {
    axios.delete(`http://localhost:8088/comment_service_api/comment/${comment.id}`, 
      {
        headers: {
          Authorization: "Bearer " + getToken(),
        },  
      }).then(res => {
        axios.get(
          `http://localhost:8088/comment_service_api/comment/category/${this.state.category}/${this.state.petId}`
        )
        .then((response) => {
            this.setState({
              ...this.state,
              comments: response.data
            }); 
            this.setState({
              ...this.state,
              updateReplyContent: '',
              updateReply: false,
              updateReplyId: -1
          });
        })
        return NotificationManager.success(res.data.message, '  ', 3000);
      }).then(() => {
        for (let i = 0; i < this.state.comments.length; i++) {
          axios.get(
            `http://localhost:8088/comment_service_api/reply/comment/${this.state.comments[i].id}`
          ).then((response) => {
            this.setState({
              ...this.state,
              reply: response.data
            });
          })
        }
      }).catch((error) => {
        return NotificationManager.error('Comment is not deleted!', '  ', 3000);
      });
    }

  deleteReply(reply) {
    axios.delete(`http://localhost:8088/comment_service_api/reply/${reply.id}`, 
      {
        headers: {
          Authorization: "Bearer " + getToken(),
        },  
      }).then(res => {
        axios.get(
          `http://localhost:8088/comment_service_api/comment/category/${this.state.category}/${this.state.petId}`
        )
        .then((response) => {
            this.setState({
              ...this.state,
              comments: response.data
            }); 
            this.setState({
              ...this.state,
              updateReplyContent: '',
              updateReply: false,
              updateReplyId: -1
            });
        })
        return NotificationManager.success(res.data.message, '  ', 3000);
      }).then(() => {
        for (let i = 0; i < this.state.comments.length; i++) {
          axios.get(
            `http://localhost:8088/comment_service_api/reply/comment/${this.state.comments[i].id}`
          ).then((response) => {
            this.setState({
              ...this.state,
              reply: response.data
            });
          })
        }
      }).catch((error) => {
        return NotificationManager.error('Reply is not deleted!', '  ', 3000);
      });
    }

    render () {
      return(
        <div className="comment-div">
          <div className="comments">
            {this.state.comments && this.state.comments.map((comment, index) => (
              <div className="comment">
                <div className="comment-info">
                    <img class="img-fouild rounded comment-avatar"
                      src={avatar}
                      alt="avatar"/>
                  <div className="info">
                    <h5> {comment.username} </h5>
                    <h4> {comment.title} </h4>
                  </div>
                </div>
                
                <p className="pt-5 pb-6">  {comment.content} </p>

                { this.state.token && comment.username === this.state.user?.username &&
                  <div className="div-btn-update">
                    <button onClick={() => this.toggleUpdateComment(comment)} className="btn px-6 py-3 bg-red-500 text-white text-center margin-auto mt-8 buttons">
                      {!this.state.updateComment ? 
                          "Update comment" : "Close"
                      }                 
                      </button>

                      <button onClick={() => this.deleteComment(comment)} className="btn px-6 py-3 bg-red-500 text-white text-center margin-auto mt-8 buttons">
                        Delete  
                      </button>
                  </div>
                }
                
                {this.state.updateComment && this.state.updateCommentId === comment.id &&
                  <div className="comment-area">
                  <form className={"form"} onSubmit={this.updateCommentFunction}>
                    <input type="text" name="updateTitle" value={this.state.updateTitle} onChange={this.handleCommentChange} placeholder="Title"/>
                    <span className={"error"}>{this.state.errors["updateTitle"]}</span>
                    <br/>
                    <textarea type="text" name="updateContent" value={this.state.updateContent} onChange={this.handleCommentChange} placeholder="Content"/>
                    <span className={"error"}>{this.state.errors["updateContent"]}</span>
                    <br/>
                    <input type="submit" value="Update" className="btn px-6 py-3 bg-red-500 text-white text-center margin-auto mt-8 buttons"/>
                  </form>
                </div>
                }

                {this.state.reply && this.state.reply.map((reply, i) => (
                  <div>
                    {
                      comment.id === reply.comment.id && 
                      <div className="reply">
                          <div className="reply-info">
                            <img class="img-fouild rounded comment-avatar"
                              src={avatar}
                              alt="avatar"/>
                          <div className="info">
                            <h5> {reply.username} </h5>
                          </div>
                        </div>
                        <p className="pt-5 pb-6"> {reply.content} </p>
                        
                        {
                          reply.username === this.state.user?.username &&
                          <>
                            <button onClick={() => this.toggleUpdateReply(reply)} className="btn px-6 py-3 bg-red-500 text-white text-center margin-auto mt-8 buttons">
                              {!this.state.updateReply ? 
                                  "Update reply" : "Close"
                              }   
                            </button>

                            <button onClick={() => this.deleteReply(reply)} className="btn px-6 py-3 bg-red-500 text-white text-center margin-auto mt-8 buttons">
                              Delete  
                            </button>
                          </>
                      
                        }

                          {this.state.updateReply && this.state.updateReplyId === reply.id &&
                            <div className="uprade-reply-area">
                            <form className={"form"} onSubmit={this.updateReplyFunction}>
                              <textarea type="text" name="updateReplyContent" value={this.state.updateReplyContent} onChange={this.handleCommentChange} placeholder="Content"/>
                              <span className={"error"}>{this.state.errors["updateReplyContent"]}</span>
                              <br/>
                              <input type="submit" value="Update" className="btn px-6 py-3 bg-red-500 text-white text-center margin-auto mt-8 buttons"/>
                            </form>
                          </div>
                          }

                      </div>
                    }
                  </div>
                ))}
                { this.state.token &&
                  <div className="div-btn-reply">
                    <button onClick={() => this.toggleReply(comment.id)} className="btn px-6 py-3 bg-red-500 text-white text-center margin-auto mt-8 buttons">Add reply </button>
                  </div>
                }
               <div className="add-reply">
                  { this.state.addReply && this.state.replyCommentId === comment.id &&
                    <div>
                      <Reply commentId={comment.id} replies={this.state.reply}></Reply>
                    </div>
                  }
                </div>
              </div>
            ))}

            { this.state.token &&
              <div className="div-btn-comment mb-10">
                <button onClick={this.toggleComment} className="btn px-6 py-3 bg-red-500 text-white text-center margin-auto mt-8 buttons">Add comment </button>
              </div>
             }
            <div className="add-comment mb-10">
              { this.state.addComment &&
                <div className="comment-area">
                  <form className={"form"} onSubmit={this.submitComment}>
                    <input type="text" name="commentTitle" value={this.state.commentTitle} onChange={this.handleCommentChange} placeholder="Title"/>
                    <span className={"error"}>{this.state.errors["commentTitle"]}</span>
                    <br/>
                    <textarea type="text" name="commentContent"  value={this.state.commentContent} onChange={this.handleCommentChange} placeholder="Content"/>
                    <span className={"error"}>{this.state.errors["commentContent"]}</span>
                    <br/>
                    <input type="submit" value="Submit" className="btn px-6 py-3 bg-red-500 text-white text-center margin-auto mt-8 buttons"/>
                  </form>
                </div>
              }
            </div>
          </div>

          <NotificationContainer/>  
        </div>
      );
    }
}

export default Comment
