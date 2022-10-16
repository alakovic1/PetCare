import React, { useState, useContext } from 'react'
import useFetch from '../hooks/useFetch'
import { useHistory, Link } from 'react-router-dom'

import Breadcrumbs from '../components/Breadcrumbs'
import Loader from '../components/Loader'
import axios from "axios";
import Comment from '../components/Comment'
import { Badge, Modal } from '@material-ui/core';
import CloseIcon from '@material-ui/icons/Close';
import { getToken, getUser, logoutUser } from "../utilities/Common";
import {NotificationContainer, NotificationManager} from 'react-notifications';

export default function Pet({id}) {
    const {response: pet, error } = useFetch(`http://localhost:8088/pet_category_service_api/pet/${id}`)

    const [message, setMessage] = React.useState('');
    const [adoptModal, setAdoptModal] = React.useState(false);
    const history = useHistory();

    const handleAdoptCart = () => {
        setAdoptModal(false);
        axios.post(
            `http://localhost:8088/adopt_service_api/eurekaa/adoption-request/${id}`,
            {"jwt":getToken(),
                message: message
            },
            {
              headers: {
                 Authorization: "Bearer " + getToken(),
              }, 
            }
          )
        .then((response) => {
            return NotificationManager.success('Request to adopt a pet added successfully!', '  ', 3000);
        })
        .catch((error) => {
            return NotificationManager.error('Pet is not adopted', '  ', 3000);
        });
    }

    const handleOpen = (rase) => {
        setAdoptModal(true);
    }

    const handleClose = () => {
        setAdoptModal(false);
    }

    if(!pet) return <Loader/>
    if(error) return <div>Error.</div>

    const { 
        name, description
    } = pet;

    return(
        <>
            <Breadcrumbs>
                <Link to='/'>Home</Link>
                <Link to='/all-pets'>All pets</Link>
                <span>{name}</span>
            </Breadcrumbs>
            <button className="btn-sm bg-red-500 text-white w-max text-uppercase ml-20 mt-5" onClick={history.goBack}>Back</button>
            <section>
                <div className="tw-container py-16">
                   <div className="grid lg:grid-cols-2 items-center gap-16 mt-6">
                        <article id="product-info" className="capitalize flex flex-col gap-5 lg:gap-4 text-sm md:text-base">
                            <div>
                                <h2 className="font-bold">{name}</h2>
                            </div>
                            <p className="leading-loose">{description}</p>
                            <div className="w-full lg:w-full flex flex-col gap-0">
                                <hr className="my-4 md:my-6"/>
                                <p className="grid grid-cols-2">
                                    <span>Animal type: </span>
                                    <span className="font-bold text-right">{pet.rase.category.name}</span>
                                    
                                </p>
                                <hr className="my-4 md:my-6"/>
                                <p className="grid grid-cols-2">
                                    <span>Breed: </span>
                                    <span className="font-bold text-right">{pet.rase.name}</span> 
                                </p>
                                <hr className="my-4 md:my-6"/>
                                <p className="grid grid-cols-2">
                                    <span>Location: </span>
                                    <span className="font-bold text-right">{pet.location}</span> 
                                </p>
                                <hr className="my-4 md:my-6"/>
                                <p className="grid grid-cols-2">
                                    <span>Age: </span>
                                    <span className="font-bold text-right">{pet.age}</span>
                                </p>
                                <hr className="my-4 md:my-6"/>
                            </div>
                            {
                                <div class="adopt-div flex-row-reverse">
                                    <button 
                                        className="btn bg-red-500 text-white md:w-1/2 lg:w-max f-right"
                                        onClick={handleOpen}
                                    >
                                        Adopt
                                    </button>
                                </div>
                            }
                        </article>
                        <article id="product-photos">
                            <img
                                alt={pet.image}
                                className="main-product-image-sm md:main-product-image-md lg:main-product-image bg-gray-300 rounded object-cover w-full"
                                src={`${pet.image}`}
                            />
                        </article>
                    </div>
                </div>
            </section>
            <section>
                <div>
                    <Comment id={id} category={2}></Comment>
                </div>
            </section>

            <Modal
                open={adoptModal}
                onClose={handleClose}
                aria-labelledby="simple-modal-title"
                aria-describedby="simple-modal-description"
            >
                <div>  
                    <div className={'paper'}>
                        <button onClick={handleClose} style={{right: 20, position: 'absolute',}}>
                            <CloseIcon />
                        </button>
                        <div style={{paddingTop: 20,}}>
                            {getToken() ? 
                            <>
                                <h4 id="modal-title">Are you sure that you want adopt this pet?</h4>
                                
                                <textarea type="text" value={message} onChange={(e) => {setMessage(e.target.value)}} placeholder="Message"/>

                                <div>
                                    <button 
                                        className="btn bg-red-500 text-white ml-4 mt-5 delete-btn"
                                        onClick={handleClose}
                                    >
                                        Close 
                                    </button>
                                    <button 
                                        className="btn bg-red-500 text-white mt-5 delete-btn"
                                        onClick={handleAdoptCart}
                                    >
                                        Adopt 
                                    </button>
                                </div>
                            </>
                            :
                            <>
                                <h4 id="modal-title">Please, login first.</h4>
                            
                            <div>
                                <Link to={"/login"}>
                                    <button className="btn bg-red-500 text-white mt-5 delete-btn">    
                                    Login
                                    </button>
                                </Link>
                            </div>
                            </>
                        }
                            
                        </div>
                    </div>
                </div>
            </Modal>

            <NotificationContainer/>
        </>
    )
}