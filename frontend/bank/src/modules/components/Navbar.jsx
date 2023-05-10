import { Link } from "@mui/material";
import axios from "axios";
import { ACCESS_TOKEN } from '../../constants';

function Navbar() {

    const logOut = () => {
        localStorage.setItem(ACCESS_TOKEN, '');
        axios.defaults.headers.common['Authorization'] = '';
        document.location.href='/login'
    }


    return (
        <nav className="navbar navbar-expand-sm navbar-dark bg-dark mb-3">
            <div className="container">
                <a className="navbar-brand" href="/">Банк</a>
                <ul className="navbar-nav">
                    <li className="nav-item">
                        <a className="nav-link" href="/transfer">Переводы</a>
                    </li>
                    <li className="nav-item">
                        <a className="nav-link" href="/credits">Кредиты</a>
                    </li>
                    <li className="nav-item">
                        <a className="nav-link" href="/rate">Вклады</a>
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

export default Navbar;