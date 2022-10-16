import React, {useContext, useState, useEffect} from 'react'
import { useLocation, NavLink, Link, useHistory} from 'react-router-dom'
import axios from "axios";
import { getToken, getUser, logoutUser } from "../utilities/Common";
import Notification from "../components/Notification";

import '../assets/scss/other.scss'

const LoginButton = () => {
    
    return (
        <Link to="/login" >
            <button className="flex text-2xl items-center lg:text-xl gap-3 shadow-none login-btn" >
                Login
                <svg className="w-8 lg:w-7" stroke="currentColor" fill="currentColor" strokeWidth="0" viewBox="0 0 640 512" xmlns="http://www.w3.org/2000/svg"><path d="M624 208h-64v-64c0-8.8-7.2-16-16-16h-32c-8.8 0-16 7.2-16 16v64h-64c-8.8 0-16 7.2-16 16v32c0 8.8 7.2 16 16 16h64v64c0 8.8 7.2 16 16 16h32c8.8 0 16-7.2 16-16v-64h64c8.8 0 16-7.2 16-16v-32c0-8.8-7.2-16-16-16zm-400 48c70.7 0 128-57.3 128-128S294.7 0 224 0 96 57.3 96 128s57.3 128 128 128zm89.6 32h-16.7c-22.2 10.2-46.9 16-72.9 16s-50.6-5.8-72.9-16h-16.7C60.2 288 0 348.2 0 422.4V464c0 26.5 21.5 48 48 48h352c26.5 0 48-21.5 48-48v-41.6c0-74.2-60.2-134.4-134.4-134.4z"></path></svg>
            </button>
        </Link>
    )
}

const LogoutButton = () => {
    const [email, setEmail] = useState("")
    const history = useHistory()
    
    useEffect(() => {
        axios.get(
          "http://localhost:8088/user_service_api/user/me",
            {
              headers: {
                Authorization: "Bearer " + getToken(),
              },
            }
          )
        .then((response) => {
            setEmail(response.data.email)
        })}
    )

    const logout = () => {
        axios.post('http://localhost:8088/user_service_api/api/auth/logout', 
            {
              email: email
            }, 
            { 
              headers: {
                Authorization: "Bearer " + getToken(),
              },  
            }).then(res => {
                logoutUser()
                history.push("/");
          }).catch(error => {
          });
    }
  
    return (
      <button className="flex text-2xl items-center lg:text-xl gap-3" onClick={() => logout({ returnTo: window.location.origin })}>
        Log Out
        <svg className="w-8 lg:w-7" stroke="currentColor" fill="currentColor" strokeWidth="0" viewBox="0 0 640 512" xmlns="http://www.w3.org/2000/svg"><path d="M624 208H432c-8.8 0-16 7.2-16 16v32c0 8.8 7.2 16 16 16h192c8.8 0 16-7.2 16-16v-32c0-8.8-7.2-16-16-16zm-400 48c70.7 0 128-57.3 128-128S294.7 0 224 0 96 57.3 96 128s57.3 128 128 128zm89.6 32h-16.7c-22.2 10.2-46.9 16-72.9 16s-50.6-5.8-72.9-16h-16.7C60.2 288 0 348.2 0 422.4V464c0 26.5 21.5 48 48 48h352c26.5 0 48-21.5 48-48v-41.6c0-74.2-60.2-134.4-134.4-134.4z"></path></svg>
      </button>
    );
}

function Navbar() {
    const [mobileMenuOpen, setMobileMenuOpen] = useState(false);
    const [width, setWidth] = useState(window.innerWidth);
    const location = useLocation()
    const [notifications, setNotificaitons] = React.useState([]);
    const userId = (JSON.parse(getUser()))?.userId;

    useEffect(() => {
        axios.get(
          `http://localhost:8088/notification_service_api/notifications/all/unread/${userId}`,
          {
            headers: {
               Authorization: "Bearer " + getToken(),
            },
          }
        )
      .then((response) => {
        setNotificaitons(response.data);
      })
    }, [])

    useEffect(() => {
        const handleResize = () => setWidth(window.innerWidth);
        window.addEventListener("resize", handleResize);
        return () => {
            window.removeEventListener("resize", handleResize);
        };
    });

    useEffect(() => {
        if(width > 768) setMobileMenuOpen(false);
    }, [width])

    useEffect(() => {
        setMobileMenuOpen(false);
        window.scrollTo(0, 0);
    }, [location])

    return (
        <header>
            <div className="tw-container h-16 md:h-20 flex justify-between items-center">
                <Link to="/" className="flex items-center gap-2 font-semibold text-red-500">
                    <i className="fa-2x fas fa-paw"></i>
                    Pet care
                </Link>
                <nav className="hidden md:block">
                    <ul className="flex gap-8">
                        <li>
                            <NavLink to="/">Home</NavLink>
                        </li>
                        <li>
                            <NavLink to="/categories">Categories</NavLink>
                        </li>
                        <li>
                            <NavLink to="/all-pets">Find a pet</NavLink>
                        </li>
                        <li>
                            <NavLink to="/about">About</NavLink>
                        </li>
                    </ul>
                </nav>
                <div>
                    <div className="hidden lg:flex gap-8">
                        {
                            getToken() &&   
                            <>
                                <Link to="/profile" className="flex text-2xl items-center lg:text-xl gap-3 shadow-none mt-1">
                                    Profile
                                </Link>
                                </>
                        }
                        {
                            getToken() ? <LogoutButton/> : <LoginButton/>
                        }
                        {
                            getToken() &&  <Notification/>
                        }
                    </div>
                    <button onClick={() => setMobileMenuOpen(true)} className="btn-lg lg:hidden border-0 bg-transparent text-red-500">
                        <i className="fa-lg md:fa-2x fas fa-bars"></i>
                    </button>
                </div>
            </div>
            <div className={`z-20 fixed top-0 left-0 w-full h-screen bg-red-500 px-5 py-8 transition-transform duration-500 ease-in-out transform  ${mobileMenuOpen ? '' : '-translate-x-full'}`}>
                <div className="w-full h-full text-red-50">
                    <div className="flex justify-between items-center">
                        <Link to="/" className="flex items-center gap-2 font-semibold text-white">
                            <i className="fa-2x fas fa-paw"></i>
                            Pet care
                        </Link>
                        <span onClick={() => setMobileMenuOpen(false)} className="cursor-pointer text-3xl font-semibold">X</span>
                    </div>
                    <ul className="mt-7 flex flex-col gap-7 text-xl">
                        <li>
                            <NavLink to="/">Home</NavLink>
                        </li>
                        <li>
                            <NavLink to="/categories">Categories</NavLink>
                        </li>
                        <li>
                            <NavLink to="/all-pets">Find a pet</NavLink>
                        </li>
                        <li>
                            <NavLink to="/about">About</NavLink>
                        </li>
                    </ul>
                    <div className="mt-10 flex gap-8 justify-center">
                            {
                                getToken() &&   
                                <>
                                    <Link to="/profile" className="flex text-2xl items-center lg:text-xl gap-3 shadow-none mt-1">
                                        Profile
                                    </Link>
                                </>
                            }
                            {
                               
                                getToken() ? <LogoutButton/> : <LoginButton/>
                            }
                            {
                                getToken() &&   
                                    <Notification/>
                            }
                    </div>
                </div>
            </div>
        </header>
    );
  }
  
  export default Navbar;