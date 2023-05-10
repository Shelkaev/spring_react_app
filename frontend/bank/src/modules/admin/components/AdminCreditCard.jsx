import React, { useState, useEffect} from "react";
import axios from "axios";
import { ACCESS_TOKEN, API_BASE_URL } from "../../../constants";
import SetLimitForm from "./SetLimitForm";


function AdminCreditCard() {
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
                                <h4>Номер карты</h4>
                                <p>{card.cardNumber}</p>
                                <h4>Тип карты</h4>
                                <p>Кредитная</p>
                                <h4>Cтатус</h4>
                                <p>{card.cardState}</p>
                                <h4>Лимит</h4>
                                <p>{card.limit}</p>
                                <h4>Процентная ставка</h4>
                                <p>{card.creditPercent}</p>
                                <h4>Пенни</h4>
                                <p>{card.penniPercent}</p>
                                <h4>Срок действия</h4>
                                <p>{card.closeDate}</p>
                                <h5>Общая задолженность</h5>
                                <p>{Math.round(card.totalDuty)}</p>
                                <p className="mt-5">Баланс</p>
                                <h3>{Math.round(balance)} руб.</h3>
                            </div>

                        </div>
                        
                    </div>
                    <div className="col-md-8">
                        <div className="card p-4">
                            <div className="card-body">
                                <h5 className="text-center">Установить параметры</h5>

                                    <SetLimitForm cardId = {card.id}/>


                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </section>

    );
}

export default AdminCreditCard;