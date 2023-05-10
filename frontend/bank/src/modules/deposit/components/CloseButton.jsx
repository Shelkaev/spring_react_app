import { API_BASE_URL } from "../../../constants";
import React, { useState, useEffect } from "react";
import axios from "axios";
import Button from '@mui/material/Button';

function CloseButton(props) {

 
    const [cardList, setCardList] = useState([]);
    const [success, setSuccess] = useState('');
    const [message, setMessage] = useState('');

    useEffect(() => {
        axios.get(API_BASE_URL + '/cards/debit').then((resp) => {
            setCardList(resp.data);
        });

    }, []);

    const reject = () => {
        axios.post(API_BASE_URL + '/deposit/close',
            { debitCardId: cardList[0].id, depositId: props.depositId }).then((resp) => {
                setMessage(resp.data.message);
                setSuccess(resp.data.success);
            });
    }


    function checkResponse(success) {
        if (success === true){
            return (<p className="text-success">{message}</p>)
        } else {
            return(<p className="text-danger">{message}</p>)
        }
    }

    if (props.state) {
        return (
            <div className="row mt-3">
                <div className="col-md-12">
                    <Button
                        type="submit"
                        className="btn-secondary mt-2"
                        color="error"
                        variant="contained"
                        onClick={reject}
                    >
                        Закрыть вклад
                    </Button>
                </div>
                {checkResponse(success)}
            </div>

        )

    }

}

export default CloseButton;