import React, { useState, useRef, useEffect } from "react";
import { TextField, Button, Select, MenuItem, InputLabel, FormControl, Box } from '@mui/material';
import axios from "axios";
import {ACCESS_TOKEN, API_BASE_URL} from "../../../constants";
import CreditApprovedForm from "./CreditApprovedForm";



function CreditForm() {
    axios.defaults.headers.common['Authorization'] = localStorage.getItem(ACCESS_TOKEN);
    const errRef = useRef();

    const [amount, setAmount] = useState('');
    const [cardId, setCardId] = useState('');   
    const [cardList, setCardList] = useState([]);
    const [currency, setCurrency] = useState('');
    const [data, setData] = useState({});
    const [errMsg, setErrMsg] = useState('');
    const [fill, setFill] = useState(false);



    useEffect(() => {
        axios.get(API_BASE_URL + '/cards/credit/active').then((resp) => {
            setCardList(resp.data);
        });

    }, []);

    const handleForm = async (e) => {
        e.preventDefault();
        try {
            const response = await axios.post(API_BASE_URL + '/credit/details',
                { cardId: cardId, amount: amount, currency: currency }
            );
            setFill(true);
            setData(response.data);

        } catch (err) {
            if (!err?.response) {
                setErrMsg('Сервер не отвечает');
            } else if (err.response?.status === 400) {
                setErrMsg('Сумма введена некоректно');
            } else {
                setErrMsg('Попробуйте снова');
            }
            errRef.current.focus();
        }
    }



    if (fill) {
        return (
            <CreditApprovedForm data = {data} />
        )
    } else {
        return (
            <div className="container mt-5">
                <div className="row">
                    <div className="col-md-6 mx-auto">
                        <div className="card">
                            <div className="card-header">
                                <h4>Взять кредит</h4>

                            </div>
                            <div className="card-body">
                                <p ref={errRef} className="text-danger" aria-live="assertive">{errMsg}</p>
                                <form onSubmit={handleForm}>

                                    <Box sx={{ minWidth: 120 }}>
                                        <FormControl fullWidth>
                                            <InputLabel id="simple-select-label">Кредитная карта</InputLabel>
                                            <Select
                                                labelId="simple-select-label"
                                                id="simple-select"
                                                value={cardId}
                                                label="Кредитная карта"
                                                onChange={event => { setCardId(event.target.value) }}
                                            >
                                                {cardList.map((card) => (
                                                    <MenuItem value={card.id}>{card.cardNumber}</MenuItem>

                                                ))}
                                            </Select>
                                        </FormControl>
                                    </Box>

                                    <Box sx={{ minWidth: 120 }} className="mt-3">
                                        <FormControl fullWidth>
                                            <InputLabel id="simple-select-label">Валюта</InputLabel>
                                            <Select
                                                labelId="simple-select-label"
                                                id="simple-select"
                                                value={currency}
                                                label="Валюта"
                                                onChange={event => {setCurrency(event.target.value) }}
                                            >
                                                    <MenuItem value={'RU'}>RU</MenuItem>
                                                    <MenuItem value={'USD'}>USD</MenuItem>
                                                    <MenuItem value={'EUR'}>EUR</MenuItem>

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
                                        Получить
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

export default CreditForm;