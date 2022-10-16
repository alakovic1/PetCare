import React, { useState, useContext, useEffect } from 'react'
import { useHistory, Link } from 'react-router-dom'
import Breadcrumbs from '../components/Breadcrumbs'
import CategoryPanel from '../components/CategoryPanel'
import Loader from '../components/Loader'
import { Modal } from '@material-ui/core';
import CloseIcon from '@material-ui/icons/Close';
import axios from "axios";
import { getToken, getUser } from "../utilities/Common";
import {NotificationContainer, NotificationManager} from 'react-notifications';

import '../assets/scss/other.scss';

export default function Rase({id}) {
    const [rase, setRase] = React.useState([]);
    let history = useHistory();
    const token = getToken();
    const userRole = (JSON.parse(getUser()))?.role;

    const [gridView, setGridView] = React.useState(true);
    const [sortIndex, setSortIndex] = React.useState(0);
    const [addRase, setAddRase] = React.useState(false);
    const [name, setName] = React.useState('');
    const [description, setDescription] = React.useState('');
    const [errors, setErrors] = React.useState({});
    const [deleteModal, setDeleteModal] = React.useState(false);
    const [choosenRase, setChoosenRase] = React.useState({});

    useEffect(() => {
        axios.get(
            `http://localhost:8088/pet_category_service_api/rases/inCategory?id=${id}`
        )
        .then((response) => {
            setRase(response.data);
        })
      }, [])
    
    const sortRase = (index) => {
        if(index < 0 || index > 2) return;
        switch(index) {
           case 0:
                return rase.sort((a,b) => {
                    const aName = a.name.toLowerCase();
                    const bName = b.name.toLowerCase();
                    if(aName < bName) return -1;
                    if(aName > bName) return 1;
                    return 0;
                });
            case 1:
                return rase.sort((a,b) => {
                    const aName = a.name.toLowerCase();
                    const bName = b.name.toLowerCase();
                    if(aName < bName) return -1;
                    if(aName > bName) return 1;
                    return 0;
                }).reverse();
            default:
                return;
        }
    }

    const handleValidation = () => {
        let errors = {};
        let formIsValid = true;
  
        //name
        if(!name){
           formIsValid = false;
           errors["name"] = "This field cannot be empty!";
        }
  
        else if(name.length < 2 ){
          formIsValid = false;
          errors["name"] = "Name min length is 2!";
       }
  
        else if(name.length > 50 ){
          formIsValid = false;
          errors["name"] = "Name max length is 50!";
        }
  
        //description
        if(!description){
          formIsValid = false;
          errors["description"] = "This field cannot be empty!";
       }
  
       //setErrors({errors: errors});
       return formIsValid;
     }
    

    const addRaseFunction = () => {
        if (handleValidation()) {
          axios
            .post(
              "http://localhost:8088/pet_category_service_api/rase",
              {
                category_id: id,
                name: name,
                description: description,
              },
              {
                  headers: {
                    Authorization: "Bearer " + getToken(),
                  }  
              })
            .then((response) => {
                window.location.reload();
                return NotificationManager.success('Rase added!', '  ', 3000);
            })
            .catch((error) => {
                return NotificationManager.error(error.response.data.details[0], '  ', 3000);
            });
        }
        else return;
    }

    const deleteRase = (rase) => {
        setDeleteModal(false);
        axios.delete(
            `http://localhost:8088/pet_category_service_api/rase?id=${rase.id}`, 
            {
              headers: {
                Authorization: "Bearer " + getToken(),
              },  
        })
        .then((response) => {
            window.location.reload();
            return NotificationManager.success('Rase deleted!', '  ', 3000);
        }).catch((error) => {
            return NotificationManager.error(error.response.data.details[0], '  ', 3000);
        });
    }

    const handleOpen = (rase) => {
        setDeleteModal(true);
        setChoosenRase(rase);
    }

    const handleClose = () => {
        setDeleteModal(false);
    }

    if(!rase) return <Loader/>

    return(
        <>
            <Breadcrumbs>
                <Link to='/'>Home</Link>
                <Link to='/categories'>Categories</Link>
                <span>Rases</span>
            </Breadcrumbs>
            <button className="btn-sm bg-red-500 text-white w-max text-uppercase ml-20 mt-5" onClick={history.goBack}>Back</button>
            <section>
                <div className="tw-container py-10 flex flex-col md:flex-row gap-10">
                    <section id="filters">
                        <div className="sticky top-10 flex flex-col gap-4">
                        
                        </div>
                    </section>
                    <section id="products" className="w-full">
                        <article id="list-header" className={"flex flex-col md:grid md:items-center gap-y-2 gap-x-6"} style={{gridTemplateColumns: 'auto auto 1fr auto'}}>
                            <div className="btn-container">
                                <button 
                                    className={`border border-black rounded w-6 md:w-7 p-1 `}
                                    onClick={() => setGridView(true)}
                                >
                                    <svg stroke="currentColor" fill="currentColor" strokeWidth="0" viewBox="0 0 16 16" xmlns="http://www.w3.org/2000/svg"><path fillRule="evenodd" d="M1 2.5A1.5 1.5 0 012.5 1h3A1.5 1.5 0 017 2.5v3A1.5 1.5 0 015.5 7h-3A1.5 1.5 0 011 5.5v-3zm8 0A1.5 1.5 0 0110.5 1h3A1.5 1.5 0 0115 2.5v3A1.5 1.5 0 0113.5 7h-3A1.5 1.5 0 019 5.5v-3zm-8 8A1.5 1.5 0 012.5 9h3A1.5 1.5 0 017 10.5v3A1.5 1.5 0 015.5 15h-3A1.5 1.5 0 011 13.5v-3zm8 0A1.5 1.5 0 0110.5 9h3a1.5 1.5 0 011.5 1.5v3a1.5 1.5 0 01-1.5 1.5h-3A1.5 1.5 0 019 13.5v-3z" clipRule="evenodd"></path></svg>
                                </button>
                                <button 
                                    className={`border border-black rounded w-6 md:w-7 p-1 `}
                                    onClick={() => setGridView(false)}
                                >
                                    <svg stroke="currentColor" fill="currentColor" strokeWidth="0" viewBox="0 0 16 16" xmlns="http://www.w3.org/2000/svg"><path fillRule="evenodd" d="M2.5 11.5A.5.5 0 013 11h10a.5.5 0 010 1H3a.5.5 0 01-.5-.5zm0-4A.5.5 0 013 7h10a.5.5 0 010 1H3a.5.5 0 01-.5-.5zm0-4A.5.5 0 013 3h10a.5.5 0 010 1H3a.5.5 0 01-.5-.5z" clipRule="evenodd"></path></svg>
                                </button>
                            </div>
                            <hr/>
                            <form>
                                <label>Sort By: </label>
                                <select className="px-2 py-1 sort-select" onChange={(e) => setSortIndex(parseInt(e.target.value))}>
                                    <option value={0}>Name (A-Z)</option>
                                    <option value={1}>Name (Z-A)</option>
                                </select>
                            </form>
                            { token && userRole === "admin" &&
                                <>
                                    <div className="div-btn-update">
                                        <button onClick={() => setAddRase(!addRase)} className="btn px-6 py-3 bg-red-500 text-white text-center margin-auto mt-8 buttons">
                                            {!addRase ? 
                                                "Add rase" : "Close"
                                            }                 
                                        </button>
                                    </div>
                                </>
                            }
                        </article>

                        {addRase && 
                            <div className="add-category-area">
                                <form className={"form"} >
                                    <input type="text" name="name" value={name} onChange={(e) => setName(e.target.value)} placeholder="Name"/>
                                    <span className={"error"}>{errors["name"]}</span>
                                    <br/>
                                    <textarea type="text" name="description" value={description} onChange={(e) => setDescription(e.target.value)} placeholder="Description"/>
                                    <span className={"error"}>{errors["description"]}</span>
                                    <br/>
                                    <input onClick={(e) => {e.preventDefault(); addRaseFunction()}} type="submit" value="Add" className="btn px-6 py-3 bg-red-500 text-white text-center margin-auto mt-8 buttons"/>
                                </form>
                            </div>
                        }

                        <article id="products-list">
                            <div className={`mt-6 ${gridView ? "grid grid-cols-1 lg:grid-cols-2 xl:grid-cols-3 gap-8" : ''}`}>
                                {sortRase(sortIndex).map((rase, index) => (
                                    <div>
                                        <CategoryPanel
                                            key={index}
                                            {...rase}
                                            grid={gridView}
                                            elements={"pets/inRase"}
                                        />
                                        {token && userRole === "admin" &&
                                            <button 
                                                className="btn bg-red-500 text-white delete-btn"
                                                onClick={() => handleOpen(rase)}
                                            >
                                                Delete
                                            </button>
                                        }
                                    </div>
                                )) 
                            }
                            </div>
                        </article>
                    </section>
                </div>
            </section>

            <Modal
                open={deleteModal}
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
                            <h4 id="modal-title">Are you sure that you want delete this rase?</h4>
                            
                            <div>
                                <button 
                                    className="btn bg-red-500 text-white ml-4 mt-5 delete-btn"
                                    onClick={handleClose}
                                >
                                    Close 
                                </button>
                                <button 
                                    className="btn bg-red-500 text-white mt-5 delete-btn"
                                    onClick={() => deleteRase(choosenRase)}
                                >
                                    Delete 
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </Modal>

            <NotificationContainer/>  
        </>
    )
}