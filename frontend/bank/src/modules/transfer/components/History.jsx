import React, { useState, useEffect } from "react";
import axios from "axios";
import {ACCESS_TOKEN, API_BASE_URL} from "../../../constants";



function History(props) {
    const [data, setData] = useState([]);

    axios.defaults.headers.common['Authorization'] = localStorage.getItem(ACCESS_TOKEN);

    useEffect(() => {
        axios.get(API_BASE_URL + '/cards/debit/transfers/output/history/' + window.location.pathname.match(/\d*$/)).then((resp) => {
            setData(resp.data);
        });
    }, []);

    return (
        <div>
            <table className="table table-striped">
                <thead className="thead-dark">
                    <tr>
                        <th>#</th>
                        <th>Счет получателя</th>
                        <th>Сумма, руб</th>
                        <th>Дата перевода</th>
                        <th>Комиссия, %</th>
                        <th>Статус платежа</th>
                    </tr>
                </thead>
                {data.length > 0 ? (
                    <tbody>
                        {data.map((transfer, index) => (
                            <tr>
                                <td>{index}</td>
                                <td>{transfer.recipientAccount}</td>
                                <td>{transfer.amount}</td>
                                <td>{transfer.date}</td>
                                <td>{transfer.commission * 100}</td>
                                <td>{transfer.status}</td>
                            </tr>
                        ))}
                    </tbody>
                ) : (
                    <tbody>
                        <tr>
                            <p className="mx-3 mt-4" > Не найдено ни одного перевода</p>
                        </tr>

                    </tbody>
                )}
                </table>


        </div>
    );
}

export default History;