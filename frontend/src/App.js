import './App.css';
// import App from "./App";
import React from 'react';
import {BrowserRouter, Route, Routes} from "react-router-dom";
import Edit from "./components/Pages/EditXlsxToXlsx/EditXlsxToXlsx";
import EditXlsxToXml from "./components/Pages/EditXlsxToXml/EditXlsxToXml";
import UploadXlsxToXlsx from "./components/Pages/UploadXlsxToXlsx/UploadXlsxToXlsx";
import UploadXlsxToJson from "./components/Pages/UploadXlsxToJson/UploadXlsxToJson";
import UploadXlsxToXml from "./components/Pages/UploadXlsxToXml/UploadXlsxToXml";
import Header from "./components/Header/Header";
import Footer from "./components/Footer/Footer";
import Main from "./components/Pages/Main/Main";


function App() {
    return (
        <BrowserRouter>
            <div className='app-wrapper'>
                <Header/>
                <Routes>
                    <Route path='/' element={<Main/>}/>
                    <Route path='/uploadXlsxToXlsx' element={<UploadXlsxToXlsx/>}/>
                    <Route path='/uploadXlsxToXml' element={<UploadXlsxToXml/>}/>
                    <Route path='/uploadXlsxToJson' element={<UploadXlsxToJson/>}/>
                    <Route path='/edit' element={<Edit/>}/>
                    <Route path='/editXlsxToXml' element={<EditXlsxToXml/>}/>
                    {/*<Route path='/result' element={<ResultXlsxlToJson/>}/>*/}
                </Routes>
                <Footer/>
            </div>
        </BrowserRouter>
    );
}

export default App;
