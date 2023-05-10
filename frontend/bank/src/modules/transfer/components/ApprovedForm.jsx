import React, { useState, useRef, useEffect } from "react";
import { Button } from '@mui/material';
import axios from "axios";
import { API_BASE_URL } from "../../../constants";


function ApprovedForm(props) {
    const errRef = useRef();
    const [approve, setApprove] = useState(false);
    const [success, setSuccess] = useState('');
    const [message, setMessage] = useState('');
    const [errMsg, setErrMsg] = useState('');


    const handleButton = async (e) => {
        e.preventDefault();
        try {
            const response = await axios.post(API_BASE_URL + '/transfer/debit/execute',
                { transferId: props.transferId, debitCardId: props.cardId }
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
                                <h5 className="card-title">Перевод</h5>
                                <ul className="list-group">
                                    <li className="list-group-item">
                                        <i className="fas fa-check" /> Счет списания : {props.data.account}
                                    </li>
                                    <li className="list-group-item">
                                        <i className="fas fa-check" /> Счет зачисления : {props.data.recipientAccount}
                                    </li>
                                    <li className="list-group-item">
                                        <i className="fas fa-check" /> Сумма : {props.data.amount} руб.
                                    </li>
                                    <li className="list-group-item">
                                        <i className="fas fa-check" /> Комиссия : {props.data.commission * 100} %
                                    </li>
                                </ul>
                                <Button
                                    type="submit"
                                    className="btn-danger mt-3"
                                    color="success"
                                    variant="contained"
                                    onClick={handleButton}
                                >
                                    Перевести
                                </Button>
                                <a href="/transfer" className="btn btn-secondary mx-3 mt-3">Отмена</a>
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

export default ApprovedForm;