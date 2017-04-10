import React, {PropTypes} from "react";
import {Navbar, Nav, NavItem, NavDropdown, MenuItem, Button} from "react-bootstrap";

const App = (props) => {
    return (
        <div>
            <Navbar>
                <Navbar.Header>
                    <Navbar.Brand>
                        <a href="/">Главная</a>
                    </Navbar.Brand>
                </Navbar.Header>
                <Nav>
                    <NavItem href="/lessons">Уроки</NavItem>
                    <NavItem href="/about">О проекте</NavItem>
                    <NavItem href="/contacts">Контакты</NavItem>
                </Nav>
                <Nav pullRight>
                    <NavItem href="/registration">Регистрация</NavItem>
                    <NavItem href="/login">Вход</NavItem>
                    <NavItem href="/logout">Выход</NavItem>
                </Nav>
            </Navbar>

            <div className="container">
                {props.children}
            </div>
        </div>
    )
};

export default App;

