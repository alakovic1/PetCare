import React from 'react'
import './App.css'
import {
  BrowserRouter as Router,
  Switch,
  Route,
  Link
} from 'react-router-dom'

import AppProvider from './components/AppProvider'


import Home from './pages/home'
import About from './pages/about'
import Login from './pages/login'
import Register from './pages/register'
import Profile from './pages/profile'
import PasswordRecovery from './pages/passwordRecovery'
import Categories from './pages/categories'
import Rase from './pages/rase'
import RasePets from './pages/rasePets'
import Pet from './pages/pet'
import AllPets from './pages/allPets'

// eslint-disable-next-line no-extend-native
Number.prototype.toCurrency = function(){
  return `$${(this / 100).toFixed(2).toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",")}`;
}


function App() {
  return (
    <Router>
      <AppProvider>
        <Switch>
          <Route exact path="/" component={Home}/>
          <Route 
            path="/rases/:id"
            render={(props) => <Rase {...props} {...props.match.params}/>}
          />
          <Route 
            path="/pets/inRase/:id"
            render={(props) => <RasePets {...props} {...props.match.params}/>}
          />
          <Route 
            path="/pet/:id"
            render={(props) => <Pet {...props} {...props.match.params}/>}
          />
          <Route path="/about" component={About}/>
          <Route path="/login" component={Login}/>
          <Route path="/register" component={Register}/>
          <Route path="/profile" component={Profile}/>
          <Route path="/categories" component={Categories}/>
          <Route path="/password-recovery" component={PasswordRecovery}/>
          <Route path="/all-pets" component={AllPets}/>
          <Route path="*">
            <div className="tw-container text-center py-20">
              <h2 className="font-bold">404: Page Not Found</h2>
              <Link to="/" className="mt-5 btn w-max mx-auto">Return To Home</Link>
            </div>
          </Route>
        </Switch>
      </AppProvider>
    </Router>
  );
}

export default App;
