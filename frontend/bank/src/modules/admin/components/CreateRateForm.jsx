import React, { useState, useRef } from "react";
import { TextField, Button, Checkbox, FormControlLabel } from '@mui/material';
import axios from "axios";
import { ACCESS_TOKEN, API_BASE_URL } from "../../../constants";



function CreateRateForm() {
    axios.defaults.headers.common['Authorization'] = localStorage.getItem(ACCESS_TOKEN);
    const errRef = useRef();

    const [name, setName] = useState('');
    const [maxAmount, setMaxAmount] = useState('');
    const [minAmount, setMinAmount] = useState('');
    const [percent, setPercent] = useState('');
    const [numberOfMonth, setNumberOfMonth] = useState('');
    const [hasEarlyClosed, setHasEarlyClosed] = useState(false);
    const [hasIncreased, setHasIncreased] = useState(false);
    const [hasCapitalized, setHasCapitalized] = useState(false);
    const [hasWithdrawal, setHasWithdrawal] = useState(false);
    const [errMsg, setErrMsg] = useState('');
    const [fill, setFill] = useState(false);
    const [success, setSuccess] = useState('');
    const [message, setMessage] = useState('');




    const handleForm = async (e) => {
        e.preventDefault();
        try {
            const response = await axios.post(API_BASE_URL + '/rate/admin/create',
                { name: name, maxAmount: maxAmount, minAmount: minAmount, percent: percent, numberOfMonth:numberOfMonth, hasEarlyClosed:hasEarlyClosed,
                    hasIncreased:hasIncreased, hasCapitalized:hasCapitalized, hasWithdrawal:hasWithdrawal
                }
            );
            setFill(true);
            setMessage(response.data.message);
            setSuccess(response.data.success);

        } catch (err) {
            if (!err?.response) {
                setErrMsg('Сервер не отвечает');
            } else if (err.response?.status === 400) {
                setErrMsg('Форма заполнена не корректно');
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
                                <h4 className="text-center">Создать тариф</h4>
                                <div className="card-body">
                                {checkResponse(success)}
                                </div>
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
                                <h4>Создать тариф</h4>

                            </div>
                            <div className="card-body">
                                <p ref={errRef} className="text-danger" aria-live="assertive">{errMsg}</p>
                                <form onSubmit={handleForm}>

                                    <div className="form-group">
                                        <TextField
                                            margin="normal"
                                            required
                                            fullWidth
                                            label="Название"
                                            autoFocus
                                            value={name}
                                            onChange={event => { setName(event.target.value) }}
                                        />
                                    </div>
                                    <div className="form-group">
                                        <TextField
                                            margin="normal"
                                            required
                                            fullWidth
                                            label="Процентная ставка"
                                            autoFocus
                                            value={percent}
                                            onChange={event => { setPercent(event.target.value) }}
                                        />
                                    </div>
                                    <div className="form-group">
                                        <TextField
                                            margin="normal"
                                            required
                                            fullWidth
                                            label="Максимальная сумма"
                                            autoFocus
                                            value={maxAmount}
                                            onChange={event => { setMaxAmount(event.target.value) }}
                                        />
                                    </div>
                                    <div className="form-group">
                                        <TextField
                                            margin="normal"
                                            required
                                            fullWidth
                                            label="Минимальная сумма"
                                            autoFocus
                                            value={minAmount}
                                            onChange={event => { setMinAmount(event.target.value) }}
                                        />
                                    </div>

                                    <div className="form-group">
                                        <TextField
                                            margin="normal"
                                            required
                                            fullWidth
                                            label="Срок действия в месяцах"
                                            autoFocus
                                            value={numberOfMonth}
                                            onChange={event => { setNumberOfMonth(event.target.value) }}
                                        />
                                    </div>
                                    <div className="form-group">
                                        <FormControlLabel control={
                                            <Checkbox
                                                checked={hasEarlyClosed}
                                                onChange={() => { setHasEarlyClosed(!hasEarlyClosed) }}
                                            />
                                        } label="Возможно раннее закрытие" />
                                    </div>
                                    <div className="form-group">
                                        <FormControlLabel control={
                                            <Checkbox
                                                checked={hasIncreased}
                                                onChange={() => { setHasIncreased(!hasIncreased) }}
                                            />
                                        } label="Пополняемый" />
                                    </div>
                                    <div className="form-group">
                                        <FormControlLabel control={
                                            <Checkbox
                                                checked={hasCapitalized}
                                                onChange={() => { setHasCapitalized(!hasCapitalized) }}
                                            />
                                        } label="Капитализируемый" />
                                    </div>
                                    <div className="form-group">
                                        <FormControlLabel control={
                                            <Checkbox
                                                checked={hasWithdrawal}
                                                onChange={() => { setHasWithdrawal(!hasWithdrawal) }}
                                            />
                                        } label="Возможно снятие средств" />
                                    </div>
                                    <Button
                                        type="submit"
                                        className="btn btn-primary btn-block mt-2"
                                        variant="contained"
                                    >
                                        Создать
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

export default CreateRateForm;