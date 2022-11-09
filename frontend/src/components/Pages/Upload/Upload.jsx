import css from './Upload.module.css';
import upload_img from './upload_image.png';

const Upload = () => {
    return (
        <div className={css.page}>
            <div className={css.upload}>
                <div>
                    <h1 className={css.header}>Конвертация</h1>
                    <p className={css.description}>Сервис поддерживает файлы объемом до 20МБ</p>
                    <form encType="multipart/form-data" action="/edit" method="get" className={css.form}>

                        <div className={css.file_upload_block}>
                            <label htmlFor="source_file" className={css.file_input_label}>
                                Выбрать файл
                            </label>
                            <p className={css.file_input_description}>Выберите исходный файл</p>
                            {/*<input type="file" name="source_file" id="source_file" className={css.file_input}/>*/}
                            <input type="file" id="source_file" className={css.file_input}/>
                        </div>

                        <div className={css.file_upload_block}>
                            <label htmlFor="template_file" className={css.file_input_label}>
                                Выбрать файл
                            </label>
                            <p className={css.file_input_description}>Выберите файл, с необходимой структурой</p>
                            {/*<input type="file" name="template_file" id="template_file" className={css.file_input}/>*/}
                            <input type="file" id="template_file" className={css.file_input}/>
                        </div>

                        <input type="submit" value="Отправить" className={css.send_button}/>
                    </form>
                </div>
                <img src={upload_img} className={css.image}/>
            </div>
        </div>
    );
};

export default Upload;