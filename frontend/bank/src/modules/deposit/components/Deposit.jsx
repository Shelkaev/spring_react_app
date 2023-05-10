import React, { useState, useEffect } from "react";
import axios from "axios";
import { ACCESS_TOKEN, API_BASE_URL } from "../../../constants";
import CloseButton from "./CloseButton";
import DecreaseForm from "./DecreaseForm";



function Deposit() {
    axios.defaults.headers.common['Authorization'] = localStorage.getItem(ACCESS_TOKEN);
    
    const [deposit, setDeposit] = useState({});



    useEffect(() => {
        axios.get(API_BASE_URL + window.location.pathname).then((resp) => {
            setDeposit(resp.data);
        });
    }, []);

    function checkHas(success) {
        if (success === true) {
            return (<p className="text-success">Да</p>)
        } else {
            return (<p className="text-danger">Нет</p>)
        }
    }

    function checkIncreased(success) {
        if (success === true) {
            return (<p className="text-success">Да</p>)
        } else {
            return (<p></p>)
        }
    }
    
    return (
        <section id="contact" className="py-3">
            <div className="container">
                <div className="row">
                    <div className="col-md-4">
                        <div className="card p-4">
                            <div className="card-body">
                                <h5>Тариф</h5>
                                <p>{deposit.rateName}</p>
                                <h5>Номер счета</h5>
                                <p>{deposit.accountNumber}</p>
                                <h5>Максимальная сумма</h5>
                                <p>{deposit.maxAmount}</p>
                                <h5>Минимальная сумма</h5>
                                <p>{deposit.minAmount}</p>
                                <h5>Процентная ставка</h5>
                                <p>{deposit.percent * 100} %</p>
                                <h5>Срок действия</h5>
                                <p>{deposit.closeDate}</p>
                                <h5>Возможность раннего закрытия</h5>
                                {checkHas(deposit.hasEarlyClosed)}
                                <h5>Возможность пополнения</h5>
                                {checkHas(deposit.hasIncreased)}
                                <h5>Капитализируемый</h5>
                                {checkHas(deposit.hasCapitalized)}
                                <h5>Возможность снятия</h5>
                                {checkHas(deposit.hasWithdrawal)}
                                <p className="mt-5">Баланс</p>
                                <h3>{Math.round(deposit.balance)} руб.</h3>

                                <CloseButton state = {deposit.hasEarlyClosed} depositId = {deposit.depositId}/>
                            </div>

                        </div>
                        
                    </div>
                    <div className="col-md-8">
                        <div className="card p-4">
                            <div className="card-body">
                            <DecreaseForm depositId = {deposit.depositId}/>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </section>

    );
}

export default Deposit;