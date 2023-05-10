import React, { useState, useEffect, errRef } from "react";
import axios from "axios";
import { Button } from '@mui/material';
import {ACCESS_TOKEN, API_BASE_URL} from "../../../constants";

function RateTableUser() {
    axios.defaults.headers.common['Authorization'] = localStorage.getItem(ACCESS_TOKEN);

    const [rateList, setRateList] = useState([]);
    const [fill, setFill] = useState(false);
    const [success, setSuccess] = useState('');
    const [message, setMessage] = useState('');
    const [errMsg, setErrMsg] = useState('');

    useEffect(() => {
        axios.get(API_BASE_URL + '/rate/get/rates').then((resp) => {
            setRateList(resp.data);
        });

    }, []);


    function handleButton(rateId) {
        try {
            axios.post(API_BASE_URL + '/deposit/create',
                { rateId: rateId }
            ).then((resp) => {;
            setFill(true);
            setMessage(resp.data.message);
            setSuccess(resp.data.success);
            })
        } catch (err) {
            if (!err?.response) {
                setErrMsg('Сервер не отвечает');
            }  else {
                setErrMsg('Что то пошло не так');
            }
            errRef.current.focus();
        }
    }

    function checkResponse(success) {
        if (success === true) {
            return (<p className="text-center text-success">{message}</p>)
        } else {
            return (<p className="text-center text-danger">{message}</p>)
        }
    }

    
    function checkHas(success) {
        if (success === true) {
            return (<p className="text-center text-success">Да</p>)
        } else {
            return (<p className="text-center text-danger">Нет</p>)
        }
    }



    if (fill) {
        return (
            <div className="container mt-5">
                <div className="row">
                    <div className="col-md-6 mx-auto">
                        <div className="card">
                            <div className="card-header">
                                <h4 className="text-center">Открытие вклада</h4>
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
            <section id="posts">
                <div className="container">
                    <div className="row">
                        <div className="col">
                            <div className="card ">
                                <div className="card-header">
                                    <div className="row">
                                        <div className="col-md-9">
                                            <h4>Тарифы по вкладам</h4>
                                        </div>
                                    </div>
                                </div>
                                <table className="table table-striped">
                                    <thead className="thead-dark">
                                        <tr>
                                            <th>#</th>
                                            <th>Название</th>
                                            <th>Ставка</th>
                                            <th>Время, мес</th>
                                            <th>Мин. сумма</th>
                                            <th>Макс. сумма</th>
                                            <th>Раннее закрытие</th>
                                            <th>Пополняемость</th>
                                            <th>Капитализируемлсть</th>
                                            <th>Снятие средств</th>
                                            <th></th>
                                        </tr>
                                    </thead>
                                    {rateList.length > 0 ? (
                                        <tbody>
                                            {rateList.map((rate, index) => (
                                                <tr>
                                                    <td>{index}</td>
                                                    <td>{rate.name}</td>
                                                    <td>{rate.percent}</td>
                                                    <td>{rate.numberOfMonth}</td>
                                                    <td>{rate.minAmount}</td>
                                                    <td>{rate.maxAmount}</td>
                                                    <th>{checkHas(rate.hasEarlyClosed)}</th>
                                                    <th>{checkHas(rate.hasIncreased)}</th>
                                                    <th>{checkHas(rate.hasCapitalized)}</th>
                                                    <th>{checkHas(rate.hasWithdrawal)}</th>
                                                    <td>
                                                    <Button
                                                    type="submit"
                                                    className="btn-danger mt-2"
                                                    color="success"
                                                    variant="contained"
                                                    onClick={() => {handleButton(rate.id)}}
                                                >
                                                    Открыть
                                                </Button>
                                                    </td>
                                                </tr>
                                            ))}
                                        </tbody>
                                    ) : (
                                        <tbody>
                                            <tr>
                                                <p className="mx-3 mt-4" > Не найдено ни одного тарифа</p>
                                            </tr>

                                        </tbody>
                                    )}
                                </table>
                            </div>
                        </div>
                    </div>

                </div>

            </section>

        );
    }
}

export default RateTableUser;