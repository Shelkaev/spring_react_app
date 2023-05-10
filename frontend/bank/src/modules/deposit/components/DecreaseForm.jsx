import React, { useState, useRef, useEffect } from "react";
import { TextField, Button, Select, MenuItem, InputLabel, FormControl, Box } from '@mui/material';
import axios from "axios";
import { ACCESS_TOKEN, API_BASE_URL } from "../../../constants";



function DecreaseForm(props) {
    axios.defaults.headers.common['Authorization'] = localStorage.getItem(ACCESS_TOKEN);
    const errRef = useRef();

    const [amount, setAmount] = useState('');
    const [debitCardId, setDebitCardId] = useState('');
    const [cardList, setCardList] = useState([]);

    const [errMsg, setErrMsg] = useState('');
    const [fill, setFill] = useState(false);
    const [success, setSuccess] = useState('');
    const [message, setMessage] = useState('');


    useEffect(() => {
        axios.get(API_BASE_URL + '/cards/debit').then((resp) => {
            setCardList(resp.data);
        });

    }, []);

    const handleForm = async (e) => {
        e.preventDefault();
        try {
            const response = await axios.post(API_BASE_URL + '/deposit/decrease',
                { debitCardId: debitCardId, amount: amount, depositId: props.depositId }
            );
            setFill(true);
            setMessage(response.data.message);
            setSuccess(response.data.success);

        } catch (err) {
            if (!err?.response) {
                setErrMsg('Сервер не отвечает');
            } else if (err.response?.status === 400) {
                setErrMsg('Форма заполнена некоректно');
            } else {
                setErrMsg('Попробуйте снова');
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



    if (fill) {
        return (
            <div className="container mt-5">
                <div className="row">
                    <div className="col-md-6 mx-auto">
                        <div className="card">
                            <div className="card-header">
                                <h4>Снятие средств с депозита</h4>
                                {checkResponse(success)}
                            </div>
                        </div>
                    </div>
                </div>
            </div>

        )
    } else {
        return (
            <div className="container mt-5">
                <div className="row">
                    <div className="col-md-6 mx-auto">
                        <div className="card">
                            <div className="card-header">
                                <h4>Снятие средств с депозита</h4>

                            </div>
                            <div className="card-body">
                                <p ref={errRef} className="text-danger" aria-live="assertive">{errMsg}</p>
                                <form onSubmit={handleForm}>

                                    <Box sx={{ minWidth: 120 }}>
                                        <FormControl fullWidth>
                                            <InputLabel id="simple-select-label">Карта получения</InputLabel>
                                            <Select
                                                labelId="simple-select-label"
                                                id="simple-select"
                                                value={debitCardId}
                                                label="Карта получения"
                                                onChange={event => { setDebitCardId(event.target.value) }}
                                            >
                                                {cardList.map((card) => (
                                                    <MenuItem value={card.id}>{card.cardNumber}</MenuItem>

                                                ))}
                                            </Select>
                                        </FormControl>
                                    </Box>


                                    <div className="form-group">
                                        <TextField
                                            margin="normal"
                                            required
                                            fullWidth
                                            label="Сумма"
                                            autoFocus
                                            value={amount}
                                            onChange={event => { setAmount(event.target.value) }}
                                        />
                                    </div>

                                    <Button
                                        type="submit"
                                        className="btn btn-primary btn-block mt-2"
                                        variant="contained"
                                    >
                                        Снять
                                    </Button>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

        )
    }

}

export default DecreaseForm;