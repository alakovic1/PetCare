import React, {useContext, useState, useEffect} from 'react'
import { Badge, Modal } from '@material-ui/core';
import NotificationsIcon from '@material-ui/icons/Notifications';
import CloseIcon from '@material-ui/icons/Close';
import { useLocation, NavLink, Link, useHistory} from 'react-router-dom'
import axios from "axios";
import { getToken, getUser, logoutUser } from "../utilities/Common";
import {NotificationContainer, NotificationManager} from 'react-notifications';

import '../assets/scss/other.scss'


class Notification extends React.Component {
    constructor(props) {
      super(props);

      this.state = {
          notifications: [],
          unreadNotification: [],
          displayNotification: false,
          interval: null,
          open: false,
          choosenNotification: {},
          choosenRequest: {},
          newPet: {},
          userRole: (JSON.parse(getUser()))?.role,
          requestUser: {}
      };

      this.toggleNotification = this.toggleNotification.bind(this);
      this.getData = this.getData.bind(this);
      this.handleOpen = this.handleOpen.bind(this);
      this.handleClose = this.handleClose.bind(this);
      this.approveRequest = this.approveRequest.bind(this);
      this.notApproveRequest = this.notApproveRequest.bind(this);
      this.deleteNotification = this.deleteNotification.bind(this);
    }

    componentDidMount() {
        this.state.interval = setInterval(this.getData, 2000);
        this.getData();
    }

    componentWillUnmount() {
        clearInterval(this.state.interval);
      }

    getData() {
        axios.get(
            `http://localhost:8088/notification_service_api/notifications/all/unread/${(JSON.parse(getUser()))?.userId}`,
            {
              headers: {
                 Authorization: "Bearer " + getToken(),
              },
            }
          )
        .then((response) => {
            this.setState({
                ...this.state,
                unreadNotification: response.data
            });
        })
        .then((response) => {
            axios.get(
                `http://localhost:8088/notification_service_api/notifications/all/${(JSON.parse(getUser()))?.userId}`,
                {
                  headers: {
                     Authorization: "Bearer " + getToken(),
                  },
                }
              )
            .then((response) => {
                this.setState({
                    ...this.state,
                    notifications: response.data
                });
            })
        })
    }

    toggleNotification() {
        this.setState({
            ...this.state,
            displayNotification: !this.state.displayNotification
        });

        if (this.state.displayNotification !== true) {
            axios.put(
                `http://localhost:8088/notification_service_api/notifications/setRead/${(JSON.parse(getUser()))?.userId}`,
                {"jwt":getToken()},
                {
                  headers: {
                     Authorization: "Bearer " + getToken(),
                  },
                }
              )
            .then((response) => {
                axios.get(
                    `http://localhost:8088/notification_service_api/notifications/all/unread/${(JSON.parse(getUser()))?.userId}`,
                    {
                      headers: {
                         Authorization: "Bearer " + getToken(),
                      },
                    }
                  )
                .then((response) => {
                    this.setState({
                        ...this.state,
                        unreadNotification: response.data
                    });
                })
            })
        } 
    }
    
    handleOpen(notification) {
        if(notification.isAddPetRequest === true) {
            axios.get(
                `http://localhost:8088/adopt_service_api/add-pet-request/${notification.requestId}`,
                {
                  headers: {
                     Authorization: "Bearer " + getToken(),
                  },
                }
              )
            .then((response) => {
                this.setState({
                    ...this.state,
                    choosenNotification: notification,
                    choosenRequest: response.data
                });
                axios.get(
                    `http://localhost:8088/pet_category_service_api/all/pet/${this.state.choosenRequest.newPetID}`,
                    {
                      headers: {
                        Authorization: "Bearer " + getToken(),
                      },
                    }
                  ).then((response) => {
                        this.setState({
                            ...this.state,
                            newPet: response.data,
                            open: true
                        });
                    }).then(() => {
                        axios.get(
                            `http://localhost:8088/user_service_api/user/username/${this.state.choosenNotification.userID}`,
                            {
                              headers: {
                                  Authorization: "Bearer " + getToken(),
                              },
                            }
                          )
                        .then((response) => {
                            this.setState({
                                ...this.state,
                                requestUser: response.data
                            });
                        })
                    })
            }).catch((error) => {
                return NotificationManager.error('Request has been deleted!', '  ', 3000);
            });
        }
        else {
            axios.get(
                `http://localhost:8088/adopt_service_api/adoption-request/${notification.requestId}`,
                {
                  headers: {
                     Authorization: "Bearer " + getToken(),
                  },
                }
              ).then((response) => {
                this.setState({
                    ...this.state,
                    choosenNotification: notification,
                    choosenRequest: response.data
                });
                axios.get(
                    `http://localhost:8088/pet_category_service_api/all/pet/${this.state.choosenRequest.petID}`,
                    {
                      headers: {
                        Authorization: "Bearer " + getToken(),
                      },
                    }
                  )
                    .then((response) => {
                        this.setState({
                            ...this.state,
                            newPet: response.data,
                            open: true
                        });
                    })
            }).catch((error) => {
                return NotificationManager.error('Request has been deleted!', '  ', 3000);
            });
        }
    }

    approveRequest() {
        if(this.state.choosenNotification.isAddPetRequest === true) {
            axios.put(
                `http://localhost:8088/adopt_service_api/add-pet-request/approved/${this.state.choosenNotification.requestId}`,
                {"jwt":getToken()},
                {
                  headers: {
                     Authorization: "Bearer " + getToken(),
                  },
                }
              )
            .then((response) => {
                this.handleClose();
                return NotificationManager.success(response.data.message, '  ', 3000);
            })
            .catch((error) => {
                return NotificationManager.error('Pet is not added', '  ', 3000);
            });
        }
        else {
            axios.put(
                `http://localhost:8088/adopt_service_api/adoption-request/approve/${this.state.choosenNotification.requestId}`,
                {"jwt":getToken()},
                {
                  headers: {
                     Authorization: "Bearer " + getToken(),
                  },
                }
              )
            .then((response) => {
                this.handleClose();
                return NotificationManager.success(response.data.message, '  ', 3000);
            })
            .catch((error) => {
                return NotificationManager.error('Pet is not adopted', '  ', 3000);
            });
        }
    }

    notApproveRequest() {
        if(this.state.choosenNotification.isAddPetRequest === true) {
            axios.put(
                `http://localhost:8088/adopt_service_api/add-pet-request/not-approved/${this.state.choosenNotification.requestId}`,
                {"jwt":getToken()},
                {
                  headers: {
                     Authorization: "Bearer " + getToken(),
                  },
                }
              )
            .then((response) => {
                this.handleClose();
                return NotificationManager.success(response.data.message, '  ', 3000);
            })
            .catch((error) => {
                return NotificationManager.error('Invalid request', '  ', 3000);
            });
        }
        else {
            axios.put(
                `http://localhost:8088/adopt_service_api/adopt-request/not-approved/${this.state.choosenNotification.requestId}`,
                {"jwt":getToken()},
                {
                  headers: {
                     Authorization: "Bearer " + getToken(),
                  },
                }
              )
            .then((response) => {
                this.handleClose();
                return NotificationManager.success(response.data.message, '  ', 3000);
            })
            .catch((error) => {
                return NotificationManager.error('Invalid request', '  ', 3000);
            });
        }
    }

    handleClose() {
        this.setState({
            ...this.state,
            open: false
        });
    }

    deleteNotification(notification) {
        axios.delete(
            `http://localhost:8088/notification_service_api/notifications/delete/${(JSON.parse(getUser()))?.userId}/${notification.id}`,
            {
              headers: {
                 Authorization: "Bearer " + getToken(),
              },
            }
          )
        .then((response) => {
            axios.get(
                `http://localhost:8088/notification_service_api/notifications/all/${(JSON.parse(getUser()))?.userId}`,
                {
                  headers: {
                     Authorization: "Bearer " + getToken(),
                  },
                }
              )
            .then((response) => {
                this.setState({
                    ...this.state,
                    notifications: response.data
                });
            })
            .then((response) => {
                //return NotificationManager.success(response.data.message, '  ', 3000);
            })
            .catch((error) => {
                //return NotificationManager.error('Notification is not deleted', '  ', 3000);
            });
        })
    }
  
    render() {
      return (
        <div className={'notification-containter'} >
           <Badge color="secondary" badgeContent={this.state.unreadNotification.length} 
                onClick={this.toggleNotification}
            >
                <NotificationsIcon />
           </Badge>

            {
                this.state.displayNotification &&
                <div className={'notifications'}>
                    <h5 className={'notification-title'}>Notifications</h5>

                    {this.state.notifications.map((notification, index) => (
                        <div key={index} className={'notification-item'}>

                        <button onClick={(e) => this.deleteNotification(notification)} style={{right: 0, position: 'absolute',}}>
                            <CloseIcon />
                        </button>
                            <p style={{width: '90%', }}> {notification.content} </p>
 
                            { 
                                notification.requestId !== -1 && (notification.content === "New request to add a pet!" ||
                                notification.content === "New request to adopt a pet!") &&
                                <p className={'see-request'} onClick={(e) => this.handleOpen(notification)}>  See request </p>
                            }
                        </div>
                    ))}

                </div>
            }
            <Modal
                open={this.state.open}
                onClose={this.handleClose}
                aria-labelledby="simple-modal-title"
                aria-describedby="simple-modal-description"
                style={{overflow: 'scroll'}}
            >
                <div>  
                    <div className={'paper'}>
                        <button onClick={this.handleClose} style={{right: 20, position: 'absolute'}}>
                            <CloseIcon />
                        </button>
                        <div style={{paddingTop: 20,}}>
                            <h2 id="modal-title">Request view</h2>
                            <p className="request-detail"><b>Message:</b> {this.state.choosenRequest?.message}</p>
                            <p className="request-detail"><b>{this.state.newPet?.name}</b></p>
                            <img
                                alt={this.state.newPet?.image}
                                className="main-product-image-sm md:main-product-image-md lg:main-product-image bg-gray-300 rounded object-cover w-full"
                                src={`${this.state.newPet?.image}`}
                            />
                            
                            <div>
                                <button 
                                    className="btn bg-red-500 text-white ml-4 mt-5 delete-btn"
                                    onClick={this.notApproveRequest}
                                >
                                    Not approve 
                                </button>
                                <button 
                                    className="btn bg-red-500 text-white mt-5 delete-btn"
                                    onClick={this.approveRequest}
                                >
                                    Approve 
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </Modal>
         
            <NotificationContainer/>
        </div>
      );
    }
  }
  
  export default Notification 
