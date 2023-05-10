import React, { useState, useEffect } from "react";
import axios from "axios";
import { ACCESS_TOKEN, API_BASE_URL } from "../../../constants";
import ProposedButtons from "./ProposedButtons";
import CreditHistory from "../../credit/components/CreditHistory";
import ChangePenniButton from "./ChangePenniButton";


function CreditCard() {
    axios.defaults.headers.common['Authorization'] = localStorage.getItem(ACCESS_TOKEN);
    
    const [card, setCard] = useState({});
    const [balance, setBalance] = useState('');


    useEffect(() => {
        axios.get(API_BASE_URL + window.location.pathname).then((resp) => {
            setCard(resp.data);
            setBalance(resp.data.balance);
        });
    }, []);

    
    return (
        <section id="contact" className="py-3">
            <div className="container">
                <div className="row">
                    <div className="col-md-4">
                        <div className="card p-4">
                            <div className="card-body">
                                <h5>Номер карты</h5>
                                <p>{card.cardNumber}</p>
                                <h5>Тип карты</h5>
                                <p>Кредитная</p>
                                <h5>Cтатус</h5>
                                <p>{card.cardState}</p>
                                <h5>Лимит</h5>
                                <p>{card.limit}</p>
                                <h5>Процентная ставка</h5>
                                <p>{card.creditPercent * 100} %</p>
                                <h5>Пенни</h5>
                                <p>{card.penniPercent * 100} %</p>
                                <h5>Валютная комиссия</h5>
                                <p>{card.currencyPercent * 100} %</p>
                                <h5>Срок действия</h5>
                                <p>{card.closeDate}</p>
                                <h5>Общая задолженность</h5>
                                <p>{Math.round(card.totalDuty)}</p>
                                <p className="mt-5">Баланс</p>
                                <h3>{Math.round(balance)} руб.</h3>
                                <ProposedButtons state = {card.cardState} cardId = {card.id}/>
                            </div>

                        </div>
                        
                    </div>
                    <div className="col-md-8">
                        <div className="card p-4">
                            <div className="card-body">
                                <h3 className="text-center">Задолженность</h3>
                                <CreditHistory/>
                            </div>
                            <ChangePenniButton note = {card.note} cardId = {card.id}/>
                        </div>
                    </div>
                </div>
            </div>
        </section>

    );
}

export default CreditCard;