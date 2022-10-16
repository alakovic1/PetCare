import React, { useEffect } from 'react'
import { Link } from 'react-router-dom'
import Breadcrumbs from '../components/Breadcrumbs'
import CategoryPanel from '../components/CategoryPanel'
import Loader from '../components/Loader'
import { Modal } from '@material-ui/core';
import CloseIcon from '@material-ui/icons/Close';
import { getToken, getUser } from "../utilities/Common";
import axios from "axios";
import {NotificationContainer, NotificationManager} from 'react-notifications';

export default function AllPets({id}) {
    const [pets, setPets] = React.useState([]);
    const token = getToken();
    const userRole = (JSON.parse(getUser()))?.role;
    const [gridView, setGridView] = React.useState(true);
    const [sortIndex, setSortIndex] = React.useState(0);
    const [query, setQuery] = React.useState('');
    const [locationIndex, setLocationIndex] = React.useState(-1);
    const locations = [
        'Zenica',
        'Sarajevo',
        'Mostar',
        'Brcko',
        'Other'
    ]
    const [age, setAge] = React.useState(25);
    const [deleteModal, setDeleteModal] = React.useState(false);
    const [choosenPet, setChoosenPet] = React.useState({});

    useEffect(() => {
        axios.get(
            `http://localhost:8088/pet_category_service_api/pets`
        )
        .then((response) => {
            setPets(response.data);
        })
      }, [])

    const filteredProducts = pets && pets.filter(pet => {
        if(
            (query.trim() !== '' && !(pet.name.toLowerCase()).includes(query.trim().toLowerCase())) ||
            ((locationIndex > -1 && locationIndex < 4) && pet.location !== locations[locationIndex]) ||
            (locations[locationIndex] === 'Other' && locations.includes(pet.location)) ||
            (pet.age > age)
        ) return false;
        return true;
    })

    const sortPet = (petss, index) => {
        if(index < 0 || index > 2) return;
        switch(index) {
           case 0:
                return petss.sort((a,b) => {
                    const aName = a.name.toLowerCase();
                    const bName = b.name.toLowerCase();
                    if(aName < bName) return -1;
                    if(aName > bName) return 1;
                    return 0;
                });
            case 1:
                return petss.sort((a,b) => {
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

    const clearFilters = () => {
        setQuery('');
        setLocationIndex(-1);
        setAge(25)
    }

    const deletePet = (pet) => {
        setDeleteModal(false);
        axios.delete(
            `http://localhost:8088/pet_category_service_api/pet/delete?id=${pet.id}`, 
            {
              headers: {
                Authorization: "Bearer " + getToken(),
              },  
        })
        .then((response) => {
            window.location.reload();
            return NotificationManager.success('Pet deleted!', '  ', 3000);
        }).catch((error) => {
            return NotificationManager.error(error.response.data.details[0], '  ', 3000);
        });
    }

    const handleOpen = (pet) => {
        setDeleteModal(true);
        setChoosenPet(pet);
    }

    const handleClose = () => {
        setDeleteModal(false); 
    }

    if(!pets) return <Loader/>

    return(
        <>
            <Breadcrumbs>
                <Link to='/'>Home</Link>
                <span>All pets</span>
            </Breadcrumbs>
            <section>
                <div className="tw-container py-10 flex flex-col md:flex-row gap-10">
                    <section id="filters">
                        <div className="sticky top-10 flex flex-col gap-4">
                            {/* Search Input */}
                            <input 
                                type="text" 
                                placeholder="Search"
                                value={query}
                                className="bg-gray-200 w-5/12 md:w-full py-1 px-2 rounded"
                                onChange={(e) => setQuery(e.target.value)}
                            />
                            {/* Location */}
                            <div id="companies-form">
                                <h5>Location</h5>
                                <select className="mt-2 sort-select" value={locationIndex} onChange={(e) => setLocationIndex(parseInt(e.target.value))}>
                                    <option value={-1}>All</option>
                                    {
                                        locations.map((location, i) => (
                                            <option key={i} value={i}>{location}</option>
                                        ))
                                    }
                                </select>
                            </div>
                            <div id="price-range">
                                <h5>Age</h5>
                                <p className="mt-2">{age}</p>
                                <input 
                                    type="range" 
                                    min={0} 
                                    max={25}
                                    onChange={(e) => setAge(parseInt(e.currentTarget.value))}
                                    value={age}
                                />
                            </div>
                            <button className="btn-sm bg-red-300 text-red-900 font-bold" onClick={clearFilters}>Clear Filters</button>
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
                            { token && 
                                <>
                                    <div className="div-btn-update">
                                        <Link  className="btn bg-red-500 text-white delete-btn" to='/profile'>Add pet</Link>
                                    </div>
                                </>
                            }
                        </article>
                        <article id="products-list">
                            <div className={`mt-6 ${gridView ? "grid grid-cols-1 lg:grid-cols-2 xl:grid-cols-3 gap-8" : ''}`}>
                                {sortPet(filteredProducts, sortIndex).map((pet, index) => (
                                        <div>
                                        <CategoryPanel
                                            key={index}
                                            {...pet}
                                            grid={true}
                                            elements={"pet"}
                                        />
                                        {token && userRole === "admin" &&
                                        <button 
                                            className="btn bg-red-500 text-white delete-btn"
                                            onClick={() => handleOpen(pet)}
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
                            <h4 id="modal-title">Are you sure that you want delete this pet?</h4>
                            
                            <div>
                                <button 
                                    className="btn bg-red-500 text-white ml-4 mt-5 delete-btn"
                                    onClick={handleClose}
                                >
                                    Close 
                                </button>
                                <button 
                                    className="btn bg-red-500 text-white mt-5 delete-btn"
                                    onClick={() => deletePet(choosenPet)}
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