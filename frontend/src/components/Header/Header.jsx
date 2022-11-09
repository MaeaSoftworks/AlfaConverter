import css from './Header.module.css';
import alfa_logo_black from './alfa-main-logo.png';
import {Link} from "react-router-dom";

const Header = () => {
    return (
        <div className={css.header}>
            <Link to="/" className={css.logo}>
                <img src={alfa_logo_black}/>
            </Link>
        </div>
    );
};

export default Header;