import React from 'react'
import { Link } from 'react-router-dom'
import Dropdown from 'react-dropdown';
import axios from "axios";
import { getToken, getUser, logoutUser } from "../utilities/Common";
import {NotificationContainer, NotificationManager} from 'react-notifications';

import Table from '@material-ui/core/Table';
import TableBody from '@material-ui/core/TableBody';
import TableCell from '@material-ui/core/TableCell';
import TableContainer from '@material-ui/core/TableContainer';
import TableHead from '@material-ui/core/TableHead';
import TableRow from '@material-ui/core/TableRow';
import Paper from '@material-ui/core/Paper';

import 'react-notifications/lib/notifications.css';
import '../assets/scss/login.scss'

class UserList extends React.Component {
    constructor(props) {
        super(props);
        
        this.state = {
          users: [],
          user: [],
          userRole: (JSON.parse(getUser()))?.role,
        };
    
      }

    componentDidMount() {
        axios.get(
          "http://localhost:8088/user_service_api/users",
          {
            headers: {
               Authorization: "Bearer " + getToken(),
            },
          }
        )
      .then((response) => {
        this.setState({
          ...this.state,
          users: response.data
      });
    })

      axios.get(
        "http://localhost:8088/user_service_api/user/me",
        {
          headers: {
            Authorization: "Bearer " + getToken(),
          },
        }
      )
    .then((response) => {
      this.setState({
        ...this.state,
        user: response.data
      });
    })
  }
    
    render() {
      return (
        <div>
        <TableContainer component={Paper}>
            <Table  aria-label="simple table">
                <TableHead>
                <TableRow>
                    <TableCell>Name</TableCell>
                    <TableCell align="left">Surname</TableCell>
                    <TableCell align="left">Username</TableCell>
                    <TableCell align="left">Email</TableCell>
                     </TableRow>
                </TableHead>
                <TableBody>
                {this.state.userRole === 'admin' ? this.state.users.map((row) => (
                    <TableRow key={row.name}>
                    <TableCell component="th" scope="row">
                        {row.name}
                    </TableCell>
                    <TableCell align="left">{row.surname}</TableCell>
                    <TableCell align="left">{row.username}</TableCell>
                    <TableCell align="left">{row.email}</TableCell>
                    </TableRow>
                ))
                :
                  <TableRow >
                    <TableCell component="th" scope="row">
                        {this.state.user.name}
                    </TableCell>
                    <TableCell align="left">{this.state.user.surname}</TableCell>
                    <TableCell align="left">{this.state.user.username}</TableCell>
                    <TableCell align="left">{this.state.user.email}</TableCell>
                  </TableRow>
              }
                </TableBody>
            </Table>
            </TableContainer>

            <NotificationContainer/>  
        </div>
      );
    }
  }
  
  export default UserList 