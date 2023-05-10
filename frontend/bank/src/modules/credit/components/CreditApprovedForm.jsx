import React, { useState, useRef, useEffect } from "react";
import { Button } from '@mui/material';
import axios from "axios";
import { API_BASE_URL } from "../../../constants";


function CreditApprovedForm(props) {
    const errRef = useRef();
    const [approve, setApprove] = useState(false);
    const [success, setSuccess] = useState('');
    const [message, setMessage] = useState('');
    const [errMsg, setErrMsg] = useState('');


    const handleButton = async (e) => {
        e.preventDefault();
        try {
            const response = await axios.post(API_BASE_URL + '/credit/get',
                props.data
            );
            setApprove(true);
            setMessage(response.data.message);
            setSuccess(response.data.success);

        } catch (err) {
            if (!err?.response) {
                setErrMsg('Сервер не отвечает');
            } else if (err.response?.status === 400) {
                setErrMsg('Неправильно заполнена форма');
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

    if (!approve) {
        return (
            <div className="container">
                <div className="row">
                    <div className="col-md-6 mx-auto">
                        <div className="card text-center">
                            <div className="card-header bg-dark text-white">
                                <h4>Подтверждение операции</h4>
                                <p>{errMsg}</p>
                            </div>
                            <div className="card-body">
                                <h5 className="card-title">Кредит</h5>
                                <ul className="list-group">
                                    <li className="list-group-item">
                                        <i className="fas fa-check" /> Сумма : {props.data.amount}
                                    </li>
                                    <li className="list-group-item">
                                        <i className="fas fa-check" /> Валюта : {props.data.currency}
                                    </li>
                                    <li className="list-group-item">
                                        <i className="fas fa-check" /> Валютная комиссия: {props.data.currencyCommission * 100} %
                                    </li>
                                    <li className="list-group-item">
                                        <i className="fas fa-check" /> Процентная ставка: {props.data.creditPercent * 100} %
                                    </li>
                                    <li className="list-group-item">
                                        <i className="fas fa-check" /> Дата погашения: {props.data.dateRepayment} 
                                    </li>
                                </ul>
                                <Button
                                    type="submit"
                                    className="btn-danger mt-3"
                                    color="success"
                                    variant="contained"
                                    onClick={handleButton}
                                >
                                    Получить
                                </Button>
                                <a href="/credits" className="btn btn-secondary mx-3 mt-3">Отмена</a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

        )
    } else {
        return (
            <div>
                <div className="container">
                    <div className="row">
                        <div className="col-md-6 mx-auto">
                            <div className="card text-center">
                                <div className="card-header bg-dark text-white">
                                    <h4>Статус операции</h4>
                                </div>
                                <div className="card-body">
                                {checkResponse(success)}
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        )
    }

}

export default CreditApprovedForm;