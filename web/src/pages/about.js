import React from 'react'
import Breadcrumbs from '../components/Breadcrumbs'
import { Link } from 'react-router-dom'

import team1 from "../assets/img/faces/amilaH.jpg";
import team2 from "../assets/img/faces/amilaL.jpg";
import team3 from "../assets/img/faces/samra.jpg";
import team4 from "../assets/img/faces/emir.jpg";

import '../assets/scss/about.scss'

const About = () => (
    <>
        <Breadcrumbs>
            <Link to="/">Home</Link>
            About
        </Breadcrumbs>
        <section id="team">
        <div class="container my-3 py-5 text-center m-auto">
            <div class="row mb-5">
                <div class="col">
                    <h1>This is us</h1>
                    <h6 class="my-3">
                        An amazing team that worked on this web application!!!
                    </h6>
                </div>
            </div>
            <div class="row team-members">
                <div class="team-card">
                    <div class="card h-100">
                        <div class="card-body">
                            <img class="img-fouild rounded mb-3 avatar"
                                src={team2}
                                alt="AmilaL"/>
                            <div class="info">    
                                <h3>Amila Laković</h3>
                                <br/>
                                <p>I just keep googling stuff and it keeps working.</p>
                                <br/>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="team-card">
                    <div class="card h-100">
                        <div class="card-body">
                            <img class="img-fouild rounded mb-3 avatar img-A"
                                src={team1}
                                alt="AmilaH"/>
                            <div class="info">   
                                <h3>Amila Hrustić</h3>
                                <br/>
                                <p> Hard work always pays out.</p>
                                <br/>
                            </div> 
                        </div>
                    </div>
                </div>
            </div>

            <div class="row team-members">
                <div class="team-card">
                    <div class="card h-100">
                        <div class="card-body">
                            <img class="img-fouild rounded mb-3 avatar"
                                src={team3}
                                alt="Samra"/>
                            <div class="info">
                                <h3>Samra Mujčinović</h3>
                                <br/>
                                <p>Meme enthusiast.</p>
                                <br/>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="team-card">
                    <div class="card h-100">
                        <div class="card-body">
                            <img class="img-fouild rounded mb-3 avatar img-E"
                                src={team4}
                                alt="Emir"/>
                            <div class="info">
                                <h3>Emir Pita</h3>
                                <br/>
                                <p>To go forward, you must backup.</p>
                                <br/>
                            </div>
                        </div>
                    </div>
                </div>
            </div>    
        </div>
    </section>
    </>
)

export default About