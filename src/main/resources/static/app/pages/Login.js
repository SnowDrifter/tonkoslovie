import React, {PropTypes} from "react";
import ReactDOM from 'react-dom';
import {Panel, FormGroup, Row, Col, ControlLabel, FormControl, Button} from "react-bootstrap";
import axios from "axios";

class Login extends React.Component {

    constructor(props) {
        super(props);

        this.state = {};
    }

    sendLogin(event) {
        event.preventDefault();

        const username = ReactDOM.findDOMNode(this.username).value;
        const password = ReactDOM.findDOMNode(this.password).value;

        axios.post('/login', {
            username: username,
            password: password
        })
            .then(function (response) {
                console.log(response);
                console.log(response.data);
            })
            .catch(function (error) {
                console.log(error);
            });
    }

    render() {
        let title = "Вход";


        return <div>
            <Panel header={title}>
                <FormGroup>
                    <Row>
                        <Col md={12}>
                            <FormGroup controlId="formControlsSelect">
                                <ControlLabel>Никнейм</ControlLabel>
                                <FormControl ref={username => {
                                    this.username = username
                                }} type="text" autoFocus/>
                            </FormGroup>
                        </Col>
                    </Row>

                    <Row>
                        <Col md={12}>
                            <FormGroup controlId="formControlsSelect">
                                <ControlLabel>Пароль</ControlLabel>
                                <FormControl ref={password => {
                                    this.password = password
                                }} type="text"/>
                            </FormGroup>
                        </Col>
                    </Row>
                </FormGroup>
                <Button className="pull-right" bsStyle="success" onClick={this.sendLogin.bind(this)}>Войти</Button>
            </Panel>
        </div>
    }
}

export default Login;