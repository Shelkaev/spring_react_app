import React, { useState, useEffect } from "react";
import axios from "axios";
import { ACCESS_TOKEN, API_BASE_URL } from "../../../constants";
import { NavLink } from "react-router-dom";

function OrderedCreditCardsTable() {
    const [data, setData] = useState([]);

    axios.defaults.headers.common['Authorization'] = localStorage.getItem(ACCESS_TOKEN);

    useEffect(() => {
        axios.get(API_BASE_URL + '/cards/credit/admin/ordered').then((resp) => {
            setData(resp.data);
        });
    }, []);



    return (
        <div>
            <div className="container">
                <div className="row">
                    <div className="col-md-12">
                        <div className="card p-4">
                            <div className="card-body">
                                <h3 className="text-center mb-3">Заявки на кредитную карту</h3>
                                <table className="table table-striped">
                                    <thead className="thead-dark">
                                        <tr>
                                            <th>#</th>
                                            <th>Логин пользователя</th>
                                            <th>Номер карты</th>
                                            <th></th>
                                        </tr>
                                    </thead>
                                    {data.length > 0 ? (
                                        <tbody>
                                            {data.map((card, index) => (
                                                <tr>
                                                    <td >{index}</td>
                                                    <td>{card.userLogin}</td>
                                                    <td>{card.cardNumber}</td>
                                                    <td>
                                                        <NavLink to={'/cards/credit/admin/' + card.cardId}
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
                                                <td>Заявки отсутствуют</td>
                                            </tr>

                                        </tbody>
                                    )}
                                </table>

                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default OrderedCreditCardsTable;