import React, { useState, useEffect } from "react";
import axios from "axios";
import {ACCESS_TOKEN, API_BASE_URL} from "../../../constants";



function CreditHistory(props) {
    const [data, setData] = useState([]);

    axios.defaults.headers.common['Authorization'] = localStorage.getItem(ACCESS_TOKEN);

    useEffect(() => {
        axios.get(API_BASE_URL + '/cards/credit/output/history/' + window.location.pathname.match(/\d*$/)).then((resp) => {
            setData(resp.data);
        });
    }, []);

    return (
        <div>
            <table className="table table-striped">
                <thead className="thead-dark">
                    <tr>
                        <th>#</th>
                        <th>Валюта</th>
                        <th>Сумма кредита</th>
                        <th>Задолженность</th>
                        <th>Ставка, %</th>
                        <th>Пенни, %</th>
                        <th>Дата погашения</th>
                    </tr>
                </thead>
                {data.length > 0 ? (
                    <tbody>
                        {data.map((credit, index) => (
                            <tr>
                                <td>{index}</td>
                                <td>{credit.currency}</td>
                                <td>{credit.amount}</td>
                                <td>{Math.round(credit.duty)} руб.</td>
                                <td>{credit.creditPercent * 100}</td>
                                <td>{credit.penniPercent * 100}</td>
                                <td>{credit.dateRepayment}</td>
                            </tr>
                        ))}
                    </tbody>
                ) : (
                    <tbody>
                        <tr>
                        <td>Кредиты не найдены</td>
                        </tr>

                    </tbody>
                )}
                </table>
        </div>
    );
}

export default CreditHistory;