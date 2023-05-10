import React, { useState, useEffect } from "react";
import axios from "axios";
import { NavLink } from "react-router-dom";
import Button from '@mui/material/Button';
import {API_BASE_URL} from "../../../constants";

function DebitCardsTable() {

    const [cardList, setCardList] = useState([]);

    useEffect(() => {
        axios.get(API_BASE_URL + '/cards/debit').then((resp) => {
            setCardList(resp.data);
        });

    }, []);


    const orderDebitCard = () => {
        axios.post(API_BASE_URL + '/cards/debit/create').then((resp) => {
            setCardList(resp.data);
        });
    }

    return (
        <section id="posts">
            <div className="container">
                <div className="row">
                    <div className="col">
                        <div className="card ">
                            <div className="card-header">
                                <div className="row">
                                    <div className="col-md-9">
                                        <h4>Дебитовые карты</h4>
                                    </div>
                                    <div className="col-md-3">
                                        <Button
                                            type="submit"
                                            className="btn btn-primary btn-block"
                                            variant="contained"
                                            onClick={orderDebitCard}
                                            disabled={cardList.length >= 3}
                                        >
                                            Заказать карту
                                        </Button>
                                    </div>
                                </div>
                            </div>
                            <table className="table table-striped">
                                <thead className="thead-dark">
                                    <tr>
                                        <th>#</th>
                                        <th>Номер карты</th>
                                        <th>Баланс</th>
                                        <th>Срок действия</th>
                                        <th></th>
                                    </tr>
                                </thead>
                                {cardList.length > 0 ? (
                                    <tbody>
                                        {cardList.map((card, index) => (
                                            <tr>
                                                <td>{index}</td>
                                                <td>{card.cardNumber}</td>
                                                <td>{Math.round(card.balance)}</td>
                                                <td>{card.closeDate}</td>
                                                <td>
                                                    <NavLink to={'/cards/debit/' + card.id}
                                                        className="btn btn-secondary"
                                                    >
                                                        Подробнее
                                                    </NavLink>
                                                </td>
                                            </tr>
                                        ))}
                                    </tbody>
                                ) : (
                                    <tbody>
                                        <tr>
                                            <p className="mx-3 mt-4" > Не найдено ни одной карты</p>
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

export default DebitCardsTable;