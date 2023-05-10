import React, { useState, useRef } from "react";
import TextField from '@mui/material/TextField';
import Button from '@mui/material/Button';
import axios from "axios";
import { API_BASE_URL } from "../../constants";

function RegistrationForm() {

  const errRef = useRef();

  const [login, setLogin] = useState('');
  const [name, setName] = useState('');
  const [surname, setSurname] = useState('');
  const [patronymic, setPatronymic] = useState('');
  const [password, setPassword] = useState('');
  const [errMsg, setErrMsg] = useState('');
  const [success, setSuccess] = useState(false);


  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const response = await axios.post(API_BASE_URL + '/registration',
        { login: login, name: name, surname: surname, patronymic: patronymic, password: password }
      );
      setPassword('')
      setSuccess(true);
    } catch (err) {
      if (!err?.response) {
        setErrMsg('Сервер не отвечает');
      } else if (err.response?.status === 400) {
        setErrMsg('Неправильно заполнен логин или пароль');
      } else {
        setErrMsg('Авторизация не пройдена');
      }
      errRef.current.focus();
    }
  }

  return (

    <>
      {success ? (
        <div className="col-md-6 mx-auto">
          <div className="card">
            <div className="card-body">
              <div className="card-body">
                <h4 className="text-right text-success">Регистрация прошла успешно</h4>
                <br />
                <br />
                <br />
                <br />
                <p>
                  <a href="/login">Вход в систему</a>
                </p>
              </div>
            </div>
          </div>
        </div>

      ) : (

        <div className="container mt-5">
          <div className="row">
            <div className="col-md-6 mx-auto">
              <div className="card">
                <div className="card-header">
                  <h4>Регистрация</h4>
                  
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
                        value={login}
                        onChange={event => { setLogin(event.target.value) }}
                      />
                    </div>
                    <div className="form-group">
                      <TextField
                        margin="normal"
                        required
                        fullWidth
                        id="name"
                        label="Имя"
                        name="name"
                        autoComplete="name"
                        autoFocus
                        value={name}
                        onChange={event => { setName(event.target.value) }}
                      />
                    </div>
                    <div className="form-group">
                      <TextField
                        margin="normal"
                        required
                        fullWidth
                        id="name"
                        label="Отчество"
                        name="patronymic"
                        autoComplete="patronymic"
                        autoFocus
                        value={patronymic}
                        onChange={event => { setPatronymic(event.target.value) }}
                      />
                    </div>
                    <div className="form-group">
                      <TextField
                        margin="normal"
                        required
                        fullWidth
                        id="name"
                        label="Фамилия"
                        name="surname"
                        autoComplete="surname"
                        autoFocus
                        value={surname}
                        onChange={event => { setSurname(event.target.value) }}
                      />
                    </div>
                    <div className="form-group">
                      <TextField
                        margin="normal"
                        required
                        fullWidth
                        name="password"
                        label="Пароль"
                        type="password"
                        id="password"
                        autoComplete="current-password"
                        value={password}
                        onChange={event => { setPassword(event.target.value) }}
                      />
                    </div>
                    <Button
                      type="submit"
                      class="btn btn-primary btn-block mt-2"
                      variant="contained"
                    >
                      Зарегистрировать
                    </Button>
                  </form>
                </div>
              </div>
            </div>


          </div>
        </div>
      )}

    </>

  );
}

export default RegistrationForm;