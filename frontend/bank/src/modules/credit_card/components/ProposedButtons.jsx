import { API_BASE_URL } from "../../../constants";
import React, { useState } from "react";
import axios from "axios";
import Button from '@mui/material/Button';

function ProposedButtons(props) {
    const [msg, setMsg] = useState('');
    const [activate, setActivate] = useState(false);
    const [hasReject, setHasReject] = useState(false);


    const approved = () => {
        axios.post(API_BASE_URL + '/cards/credit/edit',
            { status: 'ACTIVE', cardId: props.cardId }).then((resp) => {
                setMsg(resp.data.message);
                setActivate(true);
            });
    }

    const reject = () => {
        axios.post(API_BASE_URL + '/cards/credit/edit',
            { status: 'REJECTED', cardId: props.cardId }).then((resp) => {
                setMsg(resp.data.message);
                setHasReject(true);
            });
    }

    function generateMessage(){
        if (activate) {
            return (
                <div className="row mt-3">
                    <p className="text-success">Карта активирована</p>
                 </div>
            )
        }
        if (hasReject) {
            return (
                <div className="row mt-3">
                    <p className="text-danger">Карта отклонена</p>
                </div>
            )
        }
    }

    if (props.state === 'Ожидает активации') {
        return (
            <div className="row mt-3">
                <div className="col-md-6">
                    <Button
                        type="submit"
                        className="btn-secondary mt-2"
                        color="success"
                        variant="contained"
                        onClick={approved}
                    >
                        Принять
                    </Button>
                </div>
                <div className="col-md-6">
                    <Button
                        type="submit"
                        className="btn-secondary mt-2"
                        color="error"
                        variant="contained"
                        onClick={reject}
                    >
                        Отказаться
                    </Button>
                </div>
                {generateMessage()}
            </div>

        )

    }

}

export default ProposedButtons;