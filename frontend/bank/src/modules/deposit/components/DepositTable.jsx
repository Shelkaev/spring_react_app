import React, { useState, useEffect } from "react";
import axios from "axios";
import { NavLink } from "react-router-dom";
import {API_BASE_URL} from "../../../constants";

function DepositTable() {

    const [depositList, setDepositList] = useState([]);

    useEffect(() => {
        axios.get(API_BASE_URL + '/deposit').then((resp) => {
            setDepositList(resp.data);
        });

    }, []);


    return (
        <section id="posts">
            <div className="container mt-5">
                <div className="row">
                    <div className="col">
                        <div className="card ">
                            <div className="card-header">
                                <div className="row">
                                    <div className="col-md-9">
                                        <h4>Вклады</h4>
                                    </div>
                                </div>
                            </div>
                            <table className="table table-striped">
                                <thead className="thead-dark">
                                    <tr>
                                        <th>#</th>
                                        <th>Тариф</th>
                                        <th>Номер счета</th>
                                        <th>Баланс</th>
                                        <th>Срок действия</th>
                                        <th>Ставка</th>
                                        <th></th>
                                    </tr>
                                </thead>
                                {depositList.length > 0 ? (
                                    <tbody>
                                        {depositList.map((deposit, index) => (
                                            <tr>
                                                <td>{index}</td>
                                                <td>{deposit.rateName}</td>
                                                <td>{deposit.accountNumber}</td>
                                                <td>{Math.round(deposit.balance)}</td>
                                                <td>{deposit.closeDate}</td>
                                                <td>{deposit.percent}</td>

                                                <td>
                                                    <NavLink to={'/deposit/' + deposit.depositId}
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
                                            <p className="mx-3 mt-4" > Не найдено ни одного вклада</p>
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

export default DepositTable;