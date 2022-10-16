import React, {useState, useEffect} from 'react'
import { Link } from 'react-router-dom'
import PropTypes from 'prop-types';

import category1 from "../assets/img/categories/1.jpg";
import category2 from "../assets/img/categories/2.jpg";
import category3 from "../assets/img/categories/3.jpg";
import category4 from "../assets/img/categories/4.jpg";
import category5 from "../assets/img/categories/5.jpg";

const CategoryPanel = ({id, name, description, image, grid = true, elements}) => {

    const categories= [
        category1,
        category2,
        category3,
        category4,
        category5,
        ]; 

    if(grid) {
        return(
            <article>
                <div className="relative h-48 rounded">
                    <img
                        alt={name}
                        className="h-full rounded bg-black object-cover w-full"
                        src={elements === "pet" ? 
                            `${image}`
                            :  categories[Math.floor(Math.random() * (4 - 0 + 1) ) + 0]}
                    />
                    <div className="absolute top-0 left-0 w-full h-full rounded transition-opacity duration-500 ease-in-out opacity-0 hover:opacity-100 flex justify-center items-center bg-opacity-40 bg-gray-800">
                        <Link to={`/${elements}/${id}`} className="cursor-pointer relative w-10 h-10 text-white rounded-full bg-red-500 p-2.5">
                            <svg stroke="currentColor" fill="currentColor" strokeWidth="0" viewBox="0 0 512 512"><path d="M505 442.7L405.3 343c-4.5-4.5-10.6-7-17-7H372c27.6-35.3 44-79.7 44-128C416 93.1 322.9 0 208 0S0 93.1 0 208s93.1 208 208 208c48.3 0 92.7-16.4 128-44v16.3c0 6.4 2.5 12.5 7 17l99.7 99.7c9.4 9.4 24.6 9.4 33.9 0l28.3-28.3c9.4-9.4 9.4-24.6.1-34zM208 336c-70.7 0-128-57.2-128-128 0-70.7 57.2-128 128-128 70.7 0 128 57.2 128 128 0 70.7-57.2 128-128 128z"></path></svg>
                        </Link>
                    </div>
                </div>
                <footer className="flex justify-between items-center mt-4 capitalize">
                    <h6>{name}</h6>
                </footer>
                <footer className="flex justify-between items-center mt-4 mb-2">
                     <p>{description}</p>
                </footer>
            </article>
        )
    }

    return (
        <article className="flex flex-col lg:flex-row gap-4 lg:gap-8 mb-10">
            <div className="relative h-48 w-10/12 md:w-7/12 lg:w-4/12 rounded">
                <img
                    alt={name}
                    className="h-full rounded bg-black object-cover w-full"
                    //TODO dodati kad je elements === pet da preusmjeri na sliku od ljubimca
                    src={elements === "pet" ? 
                            `${image}`
                            :  categories[Math.floor(Math.random() * (4 - 0 + 1) ) + 0]}
                />
            </div>
            <footer className="w-full lg:w-8/12 flex flex-col gap-3 justify-center capitalize">
                <div>
                    <h4 className="font-bold">{name}</h4>
                </div>
                <p>{description}</p>
                <Link to={`/${elements}/${id}`} className="btn bg-red-500 text-white w-max text-xs">
                    Details
                </Link>
            </footer>
        </article>
    )
}

CategoryPanel.propTypes = {
    id: PropTypes.string.isRequired,
    name: PropTypes.string.isRequired,
    description: PropTypes.string.isRequired,
    image: PropTypes.string.isRequired,
    grid: PropTypes.bool,
    elements: PropTypes.string.isRequired,
}

export default CategoryPanel