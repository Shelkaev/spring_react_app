import Button from '@mui/material/Button';
import axios from "axios";
import {  useEffect, useRef, useState } from 'react';

import TextField from '@mui/material/TextField';
import { ACCESS_TOKEN, API_BASE_URL } from '../../constants';


function LoginForm() {

  const [data, setData] = useState({});

  const errRef = useRef();
  const [user, setUser] = useState('');
  const [roles, setRoles] = useState('');
  const [pwd, setPwd] = useState('');
  const [errMsg, setErrMsg] = useState('');
  const [success, setSuccess] = useState(false);

  useEffect(() => {
    axios.get( API_BASE_URL + '/login').then((resp) => {
      setData(resp.data);
    });
  }, []);

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      const response = await axios.post(API_BASE_URL +'/login',
        { login: user, password: pwd }
      );
      setRoles(response.data.role);
      console.log(response.data)
      localStorage.setItem(ACCESS_TOKEN, response.data.token);
      setUser(user);
      setPwd('');
      setSuccess(true);
      
    } catch (err) {
      if (!err.response) {
        setErrMsg('Сервер не отвечает');
      } else if (err.response.status === 400) {
        setErrMsg('Пропущен логин или пароль');
      } else if (err.response.status === 403) {
        setErrMsg('Не верен логин или пароль');
      } else {
        setErrMsg('Авторизация не пройдена');
      }
      errRef.current.focus();
    }
  }
  function generateLink(success) {
    if (roles === 'USER'){
        return (<a href="/">На главную страницу</a>)
    } else {
        return(<a href="/admin/transfer">На главную страницу</a>)
    }
}

  return (

    <>
      <div className="container mt-5">
        <div className="row">
          {success ? (
            <div className="col-md-6 mx-auto">
              <div className="card">
                <div className="card-body">
                  <div className="card-body">
                    <h4 className="text-right text-success">Авторизация прошла успешно</h4>
                    <br />
                    <br />
                    <br />
                    <br />
                    <p>
                      {generateLink()}
                    </p>
                  </div>
                </div>
              </div>
            </div>

          ) : (

            <div className="col-md-6 mx-auto">
              <div className="card">

                <div className="card-header">
                  <h4>Вход в систему</h4>
                </div>

                <div className="card-body">
                  <p ref={errRef} className="text-danger" aria-live="assertive">{errMsg}</p>
                  <form onSubmit={handleSubmit}>
                    <div className="form-group">
                      <TextField
                        margin="normal"
                        required
                        fullWidth
                        id="login"
                        label="Логин"
                        name="login"
                        autoComplete="login"
                        autoFocus
                        onChange={event => { setUser(event.target.value) }}
                      />

                      <TextField
                        margin="normal"
                        required
                        fullWidth
                        name="password"
                        label="Пароль"
                        type="password"
                        id="password"
                        autoComplete="current-password"
                        value={pwd}
                        onChange={event => { setPwd(event.target.value) }}
                      />
                      <Button
                        type="submit"
                        class="btn btn-primary btn-block mt-2"
                        variant="contained"

                      >
                        Войти
                      </Button>
                    </div>
                  </form>

                </div>
              </div>
            </div>
          )}


          <div className="col-md-6 mx-auto">
            <div className="card">
              <div className="card-header">
                <h4>{data.title}</h4>
              </div>
              <div className="card-body  mb-4">
                <p className="mt-5" >{data.body}</p>
                <div className="col-md-3  mt-4">
                  <Button
                    type="submit"
                    class="btn btn-secondary btn-block mt-4"
                    variant="contained"
                    href="/registration"
                  >
                    Регистрация
                  </Button>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

    </>
  );
}

export default LoginForm;