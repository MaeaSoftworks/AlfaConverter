import css from './Footer.module.css';

const Footer = () => {
    return (
        <div className={css.container}>
            <div className={css.block}/>
            <div className={css.footer}>
            <div className={css.top_section}>
                <div className={css.top_links}>
                    <a className={css.link} href='https://alfabank.ru/currency/'>Курсы валют</a>
                    <a className={css.link} href='https://alfabank.ru/make-money/investments/metall/'>Курсы металлов</a>
                    <a className={css.link} href='https://alfabank.ru/office/'>Отделения</a>
                    <a className={css.link} href='https://alfabank.ru/atm/'>Банкоматы</a>
                    <a className={css.link} href='https://online.alfabank.ru/cards-activation/'>Активация карты</a>
                </div>
                <div className={css.contacts}>
                    <p className={css.contact}>8 800 200 00 00 — Частным лицам</p>
                    <p className={css.contact}>8 800 100 77 33 — Бизнесу и ИП</p>
                </div>
            </div>
            <hr className={css.section_divider}/>
            <div className={css.bottom_section}>
                <div className={css.bottom_links}>
                    <a className={css.link} href='https://alfabank.ru/about/'>О банке</a>
                    <a className={css.link} href='https://job.alfabank.ru/'>Вакансии</a>
                    <a className={css.link} href='https://alfabank.ru/essential/'>Реквизиты</a>
                    <a className={css.link} href='https://alfabank.ru/press-office/'>Пресс-служба</a>
                    <a className={css.link} href='https://alfabank.ru/retail/tariffs/'>Тарифы и документы</a>
                    <a className={css.link} href='https://alfabank.ru/everyday/non-residents/'>Иностранным гражданам</a>
                    <a className={css.link} href='https://alfabank.ru/tenders/'>Закупки</a>
                    <a className={css.link} href='https://alfabank.ru/everyday/online/alfaclick/security/'>Безопасность</a>
                    <a className={css.link} href='https://alfabank.ru/feedback/support/'>Обратная связь</a>
                    <a className={css.link} href='https://alfabank.ru/feedback/cpa/'>Пожаловаться на звонки</a>
                    <a className={css.link} href='https://alfabank.ru/sitemap/'>Карта сайта</a>
                    <a className={css.link} href='https://alfabank.ru/lp/retail/dolpri/'>Должникам и взыскателям</a>
                    <a className={css.link} href='https://alfabank.ru/help/t/retail/'>Справочный центр</a>
                    <a className={css.link} href='https://alfabank.ru/feedback/support/reviews/'>Отзывы клиентов</a>
                </div>
                <div className={css.bottom_about}>
                    <p className={css.about}>
                        © 2001-2022. АО «Альфа-Банк», официальный сайт. Генеральная лицензия Банка России № 1326 от 16
                        января 2015 г.
                        &nbsp;
                        <a className={css.link} href='https://alfabank.ru/make-money/strahovanie_vkladov/'>АО «Альфа-Банк» является участником системы обязательного страхования вкладов.</a>
                        &nbsp;
                        <a className={css.link} href='https://alfabank.ru/about/annual_report/riskinfo/'>Информация о процентных ставках по договорам банковского вклада с физическими лицами.</a>
                        &nbsp;
                        <a className={css.link} href='http://www.e-disclosure.ru/portal/company.aspx?id=1389'>Центр раскрытия корпоративной информации.</a>
                        &nbsp;
                        <a className={css.link} href='https://alfabank.ru/about/legal/'>Информация профессионального участника рынка ценных бумаг.</a>
                        &nbsp;
                        <a className={css.link} href='https://alfabank.ru/about/shareholders/'>Информация о лицах, под контролем либо значительным влиянием которых находится Банк.</a>
                        &nbsp;Ул. Каланчевская, 27, Москва, 107078.
                    </p>
                    <p className={css.about}>
                        АО «Альфа-Банк» является оператором по обработке персональных данных, информация об обработке
                        персональных данных и сведения о реализуемых требованиях к защите персональных данных отражены в
                        <a className={css.link} href='https://alfabank.ru/about/pdn/'>Политике в отношении обработки персональных данных.</a>
                    </p>
                    <p className={css.about}>
                        <a className={css.link} href='https://alfabank.ru/about/personal_politics/'>АО «Альфа-Банк» использует файлы «cookie» с целью персонализации сервисов и повышения удобства
                        пользования веб-сайтом.</a> Если вы не хотите, чтобы ваши пользовательские данные обрабатывались,
                        пожалуйста, ограничьте их использование в своём браузере.
                    </p>
                </div>
            </div>
        </div>
        </div>
    );
};

export default Footer;