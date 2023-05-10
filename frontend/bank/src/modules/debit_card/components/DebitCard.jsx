import React, { useState, useEffect, useRef } from "react";
import axios from "axios";
import Button from '@mui/material/Button';
import { ACCESS_TOKEN, API_BASE_URL } from "../../../constants";
import History from "../../transfer/components/History";

function DebitCard() {
    axios.defaults.headers.common['Authorization'] = localStorage.getItem(ACCESS_TOKEN);
    const errRef = useRef();
    const [card, setCard] = useState({});
    const [balance, setBalance] = useState('');
    const [errMsg, setErrMsg] = useState('');
    const [cardId, setCardId] = useState('');

    useEffect(() => {
        axios.get(API_BASE_URL + window.location.pathname).then((resp) => {
            setCard(resp.data);
            setBalance(resp.data.balance);
            setCardId(resp.data.id)
        });
    }, []);

    
  const handleSubmit =() => {
    try {
      axios.post(API_BASE_URL + '/cards/debit/increase',
        {amount : '10000.0', cardId : card.id }).then((resp) => {
            setBalance(resp.data.balance);
        });
    } catch (err) {
      if (!err?.response) {
        setErrMsg('Сервер не отвечает');
      } else {
        setErrMsg('Операция не выполнена');
      }
      errRef.current.focus();
    }
  }


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
                                <p>Дебитовая</p>
                                <h4>Срок действия</h4>
                                <p>{card.closeDate}</p>
                                <br />
                                <h2>{Math.round(balance)} руб.</h2>
                                <p>{errMsg}</p>

                            </div>
                            <div className="row">
                                <div className="col-md-6">
                                    <Button
                                        type="submit"
                                        className="btn-secondary mt-2"
                                        variant="contained"
                                        onClick={handleSubmit}
                                    >
                                        Пополнить
                                    </Button>
                                </div>
                            </div>
                        </div>
                        
                    </div>
                    <div className="col-md-8">
                        <div className="card p-4">
                            <div className="card-body">
                                <h3 className="text-center">История переводов</h3>


                                    <History cardId = {cardId}/>


                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </section>

    );
}

export default DebitCard;