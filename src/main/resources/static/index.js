import App from './app/App.jsx';
import Registration from "./app/pages/Registration.js";
import Login from "./app/pages/Login.js";
import Err404 from "./app/pages/Err404.js";
import React from 'react'
import {render} from 'react-dom'
import {Router, Route, Link, browserHistory} from 'react-router'

render((
    <Router history={browserHistory}>
        <Route path="/" component={App}>
            <Route path="/registration" component={Registration}/>
            <Route path="/login" component={Login}/>
            <Route path="*" component={Err404} status={404}/>
        </Route>
    </Router>
), document.getElementById('main'));
