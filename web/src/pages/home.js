import React from 'react'
import { Link } from 'react-router-dom';
import axios from "axios";
import {NotificationContainer, NotificationManager} from 'react-notifications';

import 'react-notifications/lib/notifications.css';

import cover from '../assets/img/cover2.jpg';
import cover2 from '../assets/img/cover.jpg';
import avatar from '../assets/icon/avatar.png';
import adopt from '../assets/icon/adopt.png';
import done from '../assets/icon/done.png';

import friendship from '../assets/img/home/friendship.jpg';
import play from '../assets/img/home/play.jpeg';
import care from '../assets/img/home/care.jpg';

import '../assets/scss/other.scss'

const Panel = ({id, name, image}) => (
    <article>
        <div className="relative h-56 rounded">
            <img
                alt={name}
                className="h-full rounded bg-black object-cover w-full"
                src={image}
            />
            <div className="absolute top-0 left-0 w-full h-full rounded transition-opacity duration-500 ease-in-out opacity-0 hover:opacity-100 flex justify-center items-center bg-opacity-40 bg-gray-800">
            </div>
        </div>
        <footer className="flex justify-between items-center mt-4 capitalize">
            <h6 className="tracking-wider benefit-name">{name}</h6>
        </footer>
    </article>
)

const ServiceBox = ({title, children, content}) => (
    <div className="flex flex-col bg-red-600 px-8 py-10 gap-4 rounded-md justify-center items-center">
        <div className="relative mx-auto w-16 h-16 rounded-full bg-red-400 flex justify-center items-center p-2.5">
            {children} 
        </div>
        <div>
            <h4 className="font-semibold">{title}</h4>
            <p className="mt-2 leading-7 w-full text-sm md:text-base lg:text-lg">
                {content}
            </p>
        </div>
    </div>
)

class Home extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
          name: '',
          email: '',
          message: '',
          errors: {}
        };
    
        this.handleChange = this.handleChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
        this.handleValidation = this.handleValidation.bind(this);
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
  
        else if(this.state.name.length < 3 ){
          formIsValid = false;
          errors["name"] = "Names min length is 3!";
        }
  
       else if(this.state.name.length > 50 ){
          formIsValid = false;
          errors["name"] = "Names max length is 50!";
        }
  
       //email
       if(!this.state.email){
          formIsValid = false;
          errors["email"] = "This field cannot be empty!";
        }
  
        else if(this.state.email.length > 100 ){
          formIsValid = false;
          errors["email"] = "Emails max length is 100!";
        }

        else {               
            let lastAtPos = this.state.email.lastIndexOf('@');
            let lastDotPos = this.state.email.lastIndexOf('.');
    
            if (!(lastAtPos < lastDotPos && lastAtPos > 0 && this.state.email.indexOf('@@') === -1 && lastDotPos > 2 && (this.state.email.length - lastDotPos) > 2)) {
              formIsValid = false;
              errors["email"] = "Email should be valid!";
            }
        }    
       
        //message
        if(!this.state.message){
          formIsValid = false;
          errors["message"] = "This field cannot be empty!";
       }
 
        else if(this.state.message.length < 5) {
            formIsValid = false;
            errors["message"] = "Minimum length of the feedback is 5!";
        }
    
        this.setState({errors: errors});
        return formIsValid;
      }

    handleSubmit(event) {
      event.preventDefault();
      if (this.handleValidation()) {
        axios.post('http://localhost:8088/user_service_api/email/send', {
            name: this.state.name,
            email: this.state.email,
            message: this.state.message
        }).then(res => {
            return NotificationManager.success('Successful submit', '  ', 3000);
        }).catch((error) => {
          return NotificationManager.error('Submit error', '  ', 3000);
        });
      }
      else return;
    }

    render() {
        return (
        <>
            <section id="showcase" className="py-16">
                <div className="tw-container grid lg:grid-cols-2 place-items-center gap-x-32">
                    <article>
                        <h1 className="tracking-wider font-bold text-4xl md:text-5xl">
                            For the stray care
                        </h1>
                        <p className="mt-8 max-w-lg leading-loose ">
                            Want a new friend to play, walk or run with? 
                            Are you ready to open the door of your home for a little one? 
                            Adopt a pet and enjoy with your new best friend.
                        </p>
                        <Link to="/all-pets" className="btn px-6 py-3 bg-red-500 text-white w-full md:w-max text-center mt-8">Adopt Now</Link>
                    </article>
                    <article id="showcase-image" className="relative hidden lg:block">
                        <img 
                            alt="dining-room-example"
                            style={{
                                height: '550px',
                            }}
                            className="object-cover bg-gray-100 w-full rounded-md"
                            src={cover}
                        />
                        <img
                            alt="media-shelves-room"
                            className="z-30 bg-gray-100 transform -translate-x-2/4 absolute left-0 bottom-0  w-5/12 h-40 rounded-md"
                            src={cover2}
                        />
                    </article>
                </div>
            </section>
            <section id="featured-products" className="bg-gray-100">
                <div className="tw-container py-14">
                    <div>
                        <h2 className="text-center font-bold text-3xl lg:text-4xl">Amazing benefits!</h2>
                        <div className="mt-3 w-24 h-1 bg-red-400 mx-auto"/>
                    </div>
                   <div className="mt-16 grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-10">
                        <Panel
                            id="rec3jeKnhInKHJuz2"
                            name="Friendship"
                            image={friendship}
                        />
                        <Panel
                            id="rec7JInsuCEHgmaGe"
                            name="Play"
                            image={play}
                        />
                        <Panel
                            id="recNZ0koOqEmilmoz"
                            name="Care"
                            image={care}
                        />
                    </div>
                </div>
            </section>
            <section id="services" className="bg-red-400">
                <div className="transform py-20 xl:py-0 xl:translate-y-20 tw-container text-red-50">
                    <div className="grid grid-rows-2 lg:grid-rows-1 lg:grid-cols-2 items-center gap-y-4">
                        <h3 className="font-bold text-center text-white">Adoption process</h3>
                        <p className="leading-7 text-center">If you want to adopt a pet, take a look at the process that makes it possible below.</p>
                    </div>
                    <div className="mt-16 grid grid-cols-1 lg:grid-cols-2 xl:grid-cols-3 text-center gap-10">
                        <ServiceBox title="Login!" content="The first step to adopting a pet is registration. If you already have an account, you need to log in.">
                            <img
                                alt="avatar"
                                src={avatar}
                            />
                        </ServiceBox>
                        <ServiceBox title="Choose a pet and click 'Adopt'." content="After signing up, select your new pet and click button 'Adopt' and wait admin to approve your request.">
                            <img
                                alt="adopt"
                                src={adopt}
                            />
                        </ServiceBox>
                        <ServiceBox title="Done! You have adopted a pet!" content="Congratulations on your new friendship.">
                            <img
                                alt="done"
                                src={done}
                            />
                        </ServiceBox>
                    </div>
                </div>
            </section>
            <section id="newsletter">
                <div className="tw-container my-16 md:my-32 lg:my-64">
                    <div className="grid grid-rows-2 lg:grid-rows-1 lg:grid-cols-2 items-center w-full">
                    <iframe src="https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d2876.9594191241113!2d18.39618041534269!3d43.85666977911483!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x4758c923eeaa57a3%3A0x5cbfa583cac7da02!2sFaculty%20of%20Electrical%20Engineering!5e0!3m2!1sen!2sba!4v1620425092701!5m2!1sen!2sba" className="w-full h-full" width="600" height="450"  allowFullScreen="" title="1" loading="lazy"></iframe>    
                    <form className="m-auto mt-6 md:mt-0 grid grid-cols-1 w-full max-w-lg text-base md:text-lg lg:text-xl" style={{gridTemplateColumns: '1fr auto'}} onSubmit={this.handleSubmit}>
                        <h2 className="text-left">Have a question?</h2>
                        <p className="text-left pt-5">In case you have any questions, contact us.</p>
                        <input type="text" name="name" value={this.state.name} onChange={this.handleChange} placeholder="Name"/>
                        <span className={"error"}>{this.state.errors["name"]}</span>
                        <br/>
                        <input type="email" name="email" value={this.state.email} onChange={this.handleChange} placeholder="Email"/>
                        <span className={"error"}>{this.state.errors["email"]}</span>
                        <br/>
                        <textarea type="text" name="message" value={this.state.message} onChange={this.handleChange} placeholder="Message"/>
                        <span className={"error"}>{this.state.errors["message"]}</span>
                        <br/>
                        <button className="btn px-6 py-3 bg-red-500 text-white text-center margin-auto mt-8" type="submit">Submit</button>
                    </form>
                    </div>
                </div>
            </section>
            <NotificationContainer/>
        </>
        )
    }

}

export default Home