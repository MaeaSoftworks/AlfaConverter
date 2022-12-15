import logo from './logo.svg';
import './App.css';
// import App from "./App";
import ReactDOM from 'react-dom';
import React from 'react';
import {
    BrowserRouter, Redirect,
    Route, Routes, Navigate
} from "react-router-dom";
import Result from "./components/Pages/Result/Result";
import Edit from "./components/Pages/Edit/Edit";
import Upload from "./components/Pages/Upload/Upload";
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
                    <Route path='/upload' element={<Upload/>}/>
                    <Route path='/edit' element={<Edit/>}/>
                    <Route path='/result' element={<Result/>}/>
                </Routes>
                <Footer/>
            </div>
        </BrowserRouter>
    );
}

export default App;
