import React from "react";
import {Panel, FormGroup, Row, Col, ControlLabel, FormControl, Button} from "react-bootstrap";

const Registration = () => {
    let title = "Регистрация";

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
            </FormGroup>
            <Button className="pull-right" bsStyle="success">Сохранить</Button>
        </Panel>
    </div>
};

export default Registration;