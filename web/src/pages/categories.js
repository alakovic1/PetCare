import React, { useContext } from 'react'
import { Link } from 'react-router-dom'
import Breadcrumbs from '../components/Breadcrumbs'
import CategoryPanel from '../components/CategoryPanel'
import { Modal } from '@material-ui/core';
import CloseIcon from '@material-ui/icons/Close';
import axios from "axios";
import { getToken, getUser } from "../utilities/Common";
import {NotificationContainer, NotificationManager} from 'react-notifications';

import '../assets/scss/other.scss';

class Categories extends React.Component{

    constructor(props) {
        super(props);
        
        this.state = { 
          token: getToken(),
          userRole: (JSON.parse(getUser()))?.role,
          addCategory: false,
          name: '',
          description: '',
          categories: [],
          choosenCategory: {},
          gridView: true,
          open: false,
          errors: {}
        };

        this.sortCategories = this.sortCategories.bind(this);
        this.deleteCategory = this.deleteCategory.bind(this);
        this.toggleAddCategory = this.toggleAddCategory.bind(this);
        this.handleChange = this.handleChange.bind(this);
        this.handleValidation = this.handleValidation.bind(this);
        this.addCategoryFunction = this.addCategoryFunction.bind(this);
        this.handleOpen = this.handleOpen.bind(this);
        this.handleClose = this.handleClose.bind(this);
      }

    componentDidMount() {
        axios.get(
            "http://localhost:8088/pet_category_service_api/categories"
        )
      .then((response) => {
        this.setState({
          ...this.state,
          categories: response.data,
        });
       this.sortCategories(0);
      })
    }

    sortCategories = (index) => {
        if(index < 0 || index > 2) return;
        const sortedCategories = this.state.categories;
        switch(index) {
           case 0:
                sortedCategories.sort((a,b) => {
                    const aName = a.name.toLowerCase();
                    const bName = b.name.toLowerCase();
                    if(aName < bName) return -1;
                    if(aName > bName) return 1;
                    return 0;
                });
                this.setState({
                    ...this.state,
                    categories: sortedCategories
                });
                break;
            case 1:
                sortedCategories.sort((a,b) => {
                    const aName = a.name.toLowerCase();
                    const bName = b.name.toLowerCase();
                    if(aName < bName) return -1;
                    if(aName > bName) return 1;
                    return 0;
                }).reverse();
                this.setState({
                    ...this.state,
                    categories: sortedCategories
                });
                break;
            default:
                return;
        }
    }

    toggleAddCategory() {
        this.setState({
            ...this.state,
            addCategory: !this.state.addCategory,
        })
    }

    handleChange(event) {
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
  
        else if(this.state.name.length < 2 ){
          formIsValid = false;
          errors["name"] = "Name min length is 2!";
       }
  
        else if(this.state.name.length > 50 ){
          formIsValid = false;
          errors["name"] = "Name max length is 50!";
        }
  
        //description
        if(!this.state.description){
          formIsValid = false;
          errors["description"] = "This field cannot be empty!";
       }
  
       this.setState({errors: errors});
       return formIsValid;
     }
    
     addCategoryFunction(event) {
        event.preventDefault();
        if (this.handleValidation()) {
          axios
            .post(
              "http://localhost:8088/pet_category_service_api/category",
              {
                name: this.state.name,
                description: this.state.description,
              },
              {
                  headers: {
                    Authorization: "Bearer " + getToken(),
                  }  
              })
            .then((response) => {
                axios.get(
                    "http://localhost:8088/pet_category_service_api/categories"
                )
                .then((response) => {
                    this.setState({
                    ...this.state,
                    categories: response.data,
                    addCategory: false,
                    name: '',
                    description: ''
                    });
                    this.sortCategories(0);
                })
               return NotificationManager.success('Category added!', '  ', 3000);
            })
            .catch((error) => {
                return NotificationManager.error(error.response.data.details[0], '  ', 3000);
            });
        }
        else return;
      }

    deleteCategory(category) {
        axios.delete(
            `http://localhost:8088/pet_category_service_api/category?id=${category.id}`, 
            {
              headers: {
                Authorization: "Bearer " + getToken(),
              },  
            })
        .then((response) => {
            axios.get(
                "http://localhost:8088/pet_category_service_api/categories"
            )
          .then((response) => {
            this.setState({
              ...this.state,
              categories: response.data,
              open: false
            });
           this.sortCategories(0);
          })
          return NotificationManager.success('Category deleted!', '  ', 3000);
        }).catch((error) => {
            return NotificationManager.error(error.response.data.details[0], '  ', 3000);
        });
    }

    handleOpen(category) {
        this.setState({
            ...this.state,
            open: true,
            choosenCategory: category
        });
    }

    handleClose() { 
        this.setState({
            ...this.state,
            open: false
        });
    }

    render() {
        return(
            <>
                <Breadcrumbs>
                    <Link to='/'>Home</Link>
                    <span>Categories</span>
                </Breadcrumbs>
                <section>
                    <div className="tw-container py-10 flex flex-col md:flex-row gap-10">
                        <section id="products" className="w-full">
                            <article id="list-header" className={"flex flex-col md:grid md:items-center gap-y-2 gap-x-6"} style={{gridTemplateColumns: 'auto auto 1fr auto'}}>
                                <div className="btn-container">
                                    <button 
                                        className={`border border-black rounded w-6 md:w-7 p-1 `}
                                        onClick={() => this.setState({
                                            ...this.state,
                                            gridView: true
                                        })}
                                    >
                                        <svg stroke="currentColor" fill="currentColor" strokeWidth="0" viewBox="0 0 16 16" xmlns="http://www.w3.org/2000/svg"><path fillRule="evenodd" d="M1 2.5A1.5 1.5 0 012.5 1h3A1.5 1.5 0 017 2.5v3A1.5 1.5 0 015.5 7h-3A1.5 1.5 0 011 5.5v-3zm8 0A1.5 1.5 0 0110.5 1h3A1.5 1.5 0 0115 2.5v3A1.5 1.5 0 0113.5 7h-3A1.5 1.5 0 019 5.5v-3zm-8 8A1.5 1.5 0 012.5 9h3A1.5 1.5 0 017 10.5v3A1.5 1.5 0 015.5 15h-3A1.5 1.5 0 011 13.5v-3zm8 0A1.5 1.5 0 0110.5 9h3a1.5 1.5 0 011.5 1.5v3a1.5 1.5 0 01-1.5 1.5h-3A1.5 1.5 0 019 13.5v-3z" clipRule="evenodd"></path></svg>
                                    </button>
                                    <button 
                                        className={`border border-black rounded w-6 md:w-7 p-1 `}
                                        onClick={() => this.setState({
                                            ...this.state,
                                            gridView: false
                                        })}
                                    >
                                        <svg stroke="currentColor" fill="currentColor" strokeWidth="0" viewBox="0 0 16 16" xmlns="http://www.w3.org/2000/svg"><path fillRule="evenodd" d="M2.5 11.5A.5.5 0 013 11h10a.5.5 0 010 1H3a.5.5 0 01-.5-.5zm0-4A.5.5 0 013 7h10a.5.5 0 010 1H3a.5.5 0 01-.5-.5zm0-4A.5.5 0 013 3h10a.5.5 0 010 1H3a.5.5 0 01-.5-.5z" clipRule="evenodd"></path></svg>
                                    </button>
                                </div>
                                <hr/>
                                <form>
                                    <label>Sort By: </label>
                                    <select className="px-2 py-1 sort-select" onChange={(e) => this.sortCategories(parseInt(e.target.value))}>
                                        <option value={0}>Name (A-Z)</option>
                                        <option value={1}>Name (Z-A)</option>
                                    </select>
                                </form>
                                { this.state.token && this.state.userRole === "admin" &&
                                     <>
                                        <div className="div-btn-update">
                                            <button onClick={this.toggleAddCategory} className="btn px-6 py-3 bg-red-500 text-white text-center margin-auto mt-8 buttons">
                                                {!this.state.addCategory ? 
                                                    "Add category" : "Close"
                                                }                 
                                            </button>
                                        </div>
                                      </>
                                    }
                            </article>
                            
                            {this.state.addCategory && 
                                <div className="add-category-area">
                                    <form className={"form"} onSubmit={this.addCategoryFunction}>
                                    <input type="text" name="name" value={this.state.name} onChange={this.handleChange} placeholder="Name"/>
                                    <span className={"error"}>{this.state.errors["name"]}</span>
                                    <br/>
                                    <textarea type="text" name="description" value={this.state.description} onChange={this.handleChange} placeholder="Description"/>
                                    <span className={"error"}>{this.state.errors["description"]}</span>
                                    <br/>
                                    <input type="submit" value="Add" className="btn px-6 py-3 bg-red-500 text-white text-center margin-auto mt-8 buttons"/>
                                    </form>
                                </div>
                            }

                            <article id="products-list">
                                <div className={`mt-6 ${this.state.gridView ? "grid grid-cols-1 lg:grid-cols-2 xl:grid-cols-3 gap-8" : ''}`}>
                                    {this.state.categories.map((category, index) => (
                                        <div>
                                            <CategoryPanel
                                                key={index}
                                                {...category}
                                                grid={this.state.gridView}
                                                elements={"rases"}
                                            />
                                            {this.state.token && this.state.userRole === "admin" &&
                                                <button 
                                                    className="btn bg-red-500 text-white delete-btn"
                                                    onClick={() => this.handleOpen(category)}
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
                open={this.state.open}
                onClose={this.handleClose}
                aria-labelledby="simple-modal-title"
                aria-describedby="simple-modal-description"
            >
                <div>  
                    <div className={'paper'}>
                        <button onClick={this.handleClose} style={{right: 20, position: 'absolute',}}>
                            <CloseIcon />
                        </button>
                        <div style={{paddingTop: 20,}}>
                            <h4 id="modal-title">Are you sure that you want delete this category?</h4>
                            
                            <div>
                                <button 
                                    className="btn bg-red-500 text-white ml-4 mt-5 delete-btn"
                                    onClick={this.handleClose}
                                >
                                    Close 
                                </button>
                                <button 
                                    className="btn bg-red-500 text-white mt-5 delete-btn"
                                    onClick={() => this.deleteCategory(this.state.choosenCategory)}
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
}

export default Categories 