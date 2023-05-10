import React, { useState, useEffect } from "react";
import axios from "axios";
import { NavLink } from "react-router-dom";
import {ACCESS_TOKEN, API_BASE_URL} from "../../../constants";

function RateTable() {
    axios.defaults.headers.common['Authorization'] = localStorage.getItem(ACCESS_TOKEN);

    const [rateList, setRateList] = useState([]);

    useEffect(() => {
        axios.get(API_BASE_URL + '/rate/get/rates').then((resp) => {
            setRateList(resp.data);
        });

    }, []);


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
                                                <td>
                                                    <NavLink to={'/admin/rates/' + rate.id}
                                                        className="btn btn-secondary"
                                                    >
                                                        Редактировать
                                                    </NavLink>
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

export default RateTable;