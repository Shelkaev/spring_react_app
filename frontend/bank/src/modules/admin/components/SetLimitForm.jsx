import React, { useState, useRef } from "react";
import { Button } from '@mui/material';
import axios from "axios";
import { API_BASE_URL } from "../../../constants";
import TextField from '@mui/material/TextField';


function SetLimitForm(props) {
    const errRef = useRef();
    const [errMsg, setErrMsg] = useState('');
    const [limit, setLimit] = useState('');
    const [creditPercent, setCreditPercent] = useState('');
    const [penniPercent, setPenniPercent] = useState('');
    const [currencyPercent, setCurrencyPercent] = useState('');
    const [success, setSuccess] = useState('');
    const [message, setMessage] = useState('');
    const [d, setD] = useState(false)




    const approve = async (e) => {
        e.preventDefault();
        try {
            const response = await axios.post(API_BASE_URL + '/cards/credit/admin/parameters',
                { limit: limit, creditPercent: creditPercent, cardId: props.cardId, penniPercent: penniPercent, currencyPercent : currencyPercent});
                setMessage(response.data.message);
                setSuccess(response.data.success);
                setD(true);

        } catch (err) {
            if (!err?.response) {
                setErrMsg('Сервер не отвечает');
            } else {
                setErrMsg('Операция не выполнена');
            }
            errRef.current.focus();
        }
    }

    function checkResponse(success) {
        if (success === true){
            return (<p className="text-center text-success">{message}</p>)
        } else {
            return(<p className="text-center text-danger">{message}</p>)
        }
    }


    if (d) { return (
        <div className="container mt-5">
            <div className="row">
                <div className="col-md-6 mx-auto">
                {checkResponse(success)}
                </div>
            </div>
        </div>
    )} else {
        return (
            <div className="container mt-5">
                <div className="row">
                    <div className="col-md-6 mx-auto">
                    <p ref={errRef} className="text-danger" aria-live="assertive">{errMsg}</p>
                    <p ref={errRef} className="text-danger" aria-live="assertive">{errMsg}</p>
                        <form onSubmit={approve}>
                            <div className="form-group">

                                <TextField
                                    margin="normal"
                                    required
                                    fullWidth
                                    label="Лимит"
                                    name="limit"
                                    autoFocus
                                    onChange={event => { setLimit(event.target.value) }}
                                />
                                <TextField
                                    margin="normal"
                                    required
                                    fullWidth
                                    label="Ставка"
                                    name="limit"
                                    onChange={event => { setCreditPercent(event.target.value) }}
                                />

                                <TextField
                                    margin="normal"
                                    required
                                    fullWidth
                                    label="Пенни"
                                    name="penni"
                                    onChange={event => { setPenniPercent(event.target.value) }}
                                />

                                <TextField
                                    margin="normal"
                                    required
                                    fullWidth
                                    label="Валютная комиссия"
                                    name="currencyPercent"
                                    onChange={event => { setCurrencyPercent(event.target.value) }}
                                />


                                <Button
                                    type="submit"
                                    className="btn-secondary mt-3"
                                    variant="contained"
                                    color="success"
                                    fullWidth
                                >
                                    Подтвердить
                                </Button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>


        )

    }
}

export default SetLimitForm;

