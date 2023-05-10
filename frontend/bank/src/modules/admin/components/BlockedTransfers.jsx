import React, { useState, useEffect, useRef } from "react";
import axios from "axios";
import { ACCESS_TOKEN, API_BASE_URL } from "../../../constants";
import Button from '@mui/material/Button';

function BlockedTransfers() {
    const [data, setData] = useState([]);
    const errRef = useRef();
    const [errMsg, setErrMsg] = useState('');

    axios.defaults.headers.common['Authorization'] = localStorage.getItem(ACCESS_TOKEN);

    useEffect(() => {
        axios.get(API_BASE_URL + '/transfer/admin/blocked').then((resp) => {
            setData(resp.data);
        });
    }, []);

    const approve =(transferId) => {
        try {
          axios.post(API_BASE_URL + '/transfer/admin/blocked/execute',
            {transferId : transferId}).then((resp) => {
                setData(resp.data);
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

      const reject =(transferId) => {
        try {
          axios.post(API_BASE_URL + '/transfer/admin/blocked/reject',
            {transferId : transferId}).then((resp) => {
                setData(resp.data);
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
        <div>
            <div className="container">
                <div className="row">
                    <div className="col-md-12">
                        <div className="card p-4">
                            <div className="card-body">
                                <h3 className="text-center mb-3">Заблокированные переводы</h3>
                                <p ref={errRef} className="text-danger" aria-live="assertive">{errMsg}</p>
                                <table className="table table-striped">
                                    <thead className="thead-dark">
                                        <tr>
                                            <th>#</th>
                                            <th>Счет отправителя</th>
                                            <th>Счет получателя</th>
                                            <th>Дата перевода</th>
                                            <th>Сумма, руб</th>
                                            <th></th>
                                            <th></th>
                                        </tr>
                                    </thead>
                                    {data.length > 0 ? (
                                        <tbody>
                                            {data.map((transfer, index) => (
                                                <tr>
                                                    <td>{index}</td>
                                                    <td>{transfer.senderAccount}</td>
                                                    <td>{transfer.recipientAccount}</td>
                                                    <td>{transfer.date}</td>
                                                    <td>{transfer.amount}</td>
                                                    <td>
                                                        <Button
                                                            type="submit"
                                                            className="btn-secondary mt-2"
                                                            variant="contained"
                                                            color="success"
                                                            onClick={() => {approve(transfer.transferId)}}
                                                        >
                                                            Подтвердить
                                                        </Button>
                                                    </td>
                                                    <td>
                                                        <Button
                                                            type="submit"
                                                            className="btn-secondary mt-2"
                                                            variant="contained"
                                                            color="error"
                                                            onClick={() => {reject(transfer.transferId)}}
                                                        >
                                                            Заблокировать
                                                        </Button>
                                                    </td>
                                                </tr>
                                            ))}
                                        </tbody>
                                    ) : (
                                        <tbody>
                                            <tr>
                                            <td>Заблокированные переводы отсутствуют</td>
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

export default BlockedTransfers;