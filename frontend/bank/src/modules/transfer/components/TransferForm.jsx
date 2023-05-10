import React, { useState, useRef, useEffect } from "react";
import { TextField, Button, Select, MenuItem, InputLabel, FormControl, Box } from '@mui/material';
import axios from "axios";
import {ACCESS_TOKEN, API_BASE_URL} from "../../../constants";
import ApprovedForm from "./ApprovedForm";



function TransferForm() {
    axios.defaults.headers.common['Authorization'] = localStorage.getItem(ACCESS_TOKEN);
    const errRef = useRef();

    const [amount, setAmount] = useState('');
    const [cardId, setCardId] = useState('');
    const [cardList, setCardList] = useState([]);
    const [beneficiaryAccount, setBeneficiaryAccount] = useState('');
    const [data, setData] = useState({});
    const [errMsg, setErrMsg] = useState('');
    const [fill, setFill] = useState(false);
    const [transferId, setTransferId] = useState(false);


    useEffect(() => {
        axios.get(API_BASE_URL + '/cards/debit').then((resp) => {
            setCardList(resp.data);
        });

    }, []);

    const handleForm = async (e) => {
        e.preventDefault();
        try {
            const response = await axios.post(API_BASE_URL + '/transfer/debit/details',
                { cardId: cardId, amount: amount, beneficiaryAccount: beneficiaryAccount }
            );
            setFill(true);
            setTransferId(response.data.id)
            setData(response.data);

        } catch (err) {
            if (!err?.response) {
                setErrMsg('Сервер не отвечает');
            } else if (err.response?.status === 400) {
                setErrMsg('Счет получателя или сумма введены некорректно');
            } else {
                setErrMsg('Попробуйте снова');
            }
            errRef.current.focus();
        }
    }



    if (fill) {
        return (

            <ApprovedForm data = {data} transferId={transferId} cardId = {cardId} />
        )
    } else {
        return (
            <div className="container mt-5">
                <div className="row">
                    <div className="col-md-6 mx-auto">
                        <div className="card">
                            <div className="card-header">
                                <h4>Перевод</h4>

                            </div>
                            <div className="card-body">
                                <p ref={errRef} className="text-danger" aria-live="assertive">{errMsg}</p>
                                <form onSubmit={handleForm}>

                                    <Box sx={{ minWidth: 120 }}>
                                        <FormControl fullWidth>
                                            <InputLabel id="simple-select-label">Карта списания</InputLabel>
                                            <Select
                                                labelId="simple-select-label"
                                                id="simple-select"
                                                value={cardId}
                                                label="Карта списания"
                                                onChange={event => { setCardId(event.target.value) }}
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
                                    <div className="form-group">
                                        <TextField
                                            margin="normal"
                                            required
                                            fullWidth
                                            id="beneficiaryAccount"
                                            label="Счет получателя"
                                            name="beneficiaryAccount"
                                            autoComplete="beneficiaryAccount"
                                            autoFocus
                                            value={beneficiaryAccount}
                                            onChange={event => { setBeneficiaryAccount(event.target.value) }}
                                        />
                                    </div>

                                    <Button
                                        type="submit"
                                        className="btn btn-primary btn-block mt-2"
                                        variant="contained"
                                    >
                                        Перевести
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

export default TransferForm;