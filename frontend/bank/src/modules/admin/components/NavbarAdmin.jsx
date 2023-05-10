import { Link } from "@mui/material";
import axios from "axios";
import { ACCESS_TOKEN } from '../../../constants';

function NavbarAdmin() {

    const logOut = () => {
        localStorage.setItem(ACCESS_TOKEN, '');
        axios.defaults.headers.common['Authorization'] = '';
        document.location.href='/login'
    }


    return (
        <nav className="navbar navbar-expand-sm navbar-dark bg-dark mb-3">
            <div className="container">
                <a className="navbar-brand" href="/admin/transfer">Банк</a>
                <ul className="navbar-nav">
                    <li className="nav-item">
                        <a className="nav-link" href="/admin/transfer">Переводы</a>
                    </li>
                    <li className="nav-item">
                        <a className="nav-link" href="/admin/credits">Кредиты</a>
                    </li>
                    <li className="nav-item">
                        <a className="nav-link" href="/admin/rates">Тарифы</a>
                    </li>
                    <li className="nav-item">
                        <a className="nav-link" href="/rate/create">Создать тариф</a>
                    </li>
                    <li className="nav-item">
                    <Link
                        underline="none"
                        className="nav-link mt-2"
                        component="button"
                        onClick={() => {
                            logOut()
                        }}
                    >
                        Выйти
                    </Link>
                    </li>
                </ul>
            </div>
        </nav>
    );
}

export default NavbarAdmin;
