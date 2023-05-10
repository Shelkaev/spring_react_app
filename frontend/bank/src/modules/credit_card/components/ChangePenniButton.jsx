import { API_BASE_URL } from "../../../constants";
import React, { useState } from "react";
import axios from "axios";
import Button from '@mui/material/Button';

function ChangePenniButton(props) {
    const [msg, setMsg] = useState('');
    const [activate, setActivate] = useState(false);


    const change = () => {
        axios.post(API_BASE_URL + '/cards/credit/penni',
            { cardId: props.cardId }).then((resp) => {
                setMsg(resp.data.message);
                setActivate(true);
            });
    }
    
    function generateMessage(){
        if (activate) {
            return (
                <div className="row mt-3">
                    <p className="text-success">{msg}</p>
                 </div>
            )
        }

    }

    if (props.note === 'hasPenni') {
        return (
            <div className="row mt-3">
                <div className="col-md-6">
                <p>Запросить изменение пенни и валютной комиссии</p>
                {generateMessage()}
                    <Button
                        type="submit"
                        className="btn-secondary mt-2"
                        color="secondary"
                        variant="contained"
                        onClick={change}
                    >
                        Запросить
                    </Button>
                </div>
            </div>
        )

    }

}

export default ChangePenniButton;