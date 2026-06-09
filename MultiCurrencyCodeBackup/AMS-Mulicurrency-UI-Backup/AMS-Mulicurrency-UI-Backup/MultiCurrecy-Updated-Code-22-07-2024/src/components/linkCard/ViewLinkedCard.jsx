import React, { useState, useEffect } from "react";
import AppLayout from "../../layout/AppLayout";
import amsApi from "../../api/amsApi";
import { v4 as uuidv4 } from "uuid";
import Swal from "sweetalert2";
import { MagnifyingGlassIcon } from "@heroicons/react/24/outline";
import CustomAlert from "../../layout/CustomAlert";

export default function ViewLinkedCard() {
  const [showhide, setShowhide] = useState("");
  const [type, setType] = useState([]);
  const [accounttype, setAccountType] = useState("");
  const [accountno, setAccountno] = useState("");
  const [cardtype, setCardtype] = useState("");
  const [cardno, setCardno] = useState("");
  const [carddata, setCarddata] = useState([]);
  const [Accountdata, setData] = useState([]);
  const [Cdata, setCdata] = useState([]);
  let participantID = sessionStorage.getItem("Participantid");
  const [openAccountType, setOpenAccountType] = useState(false);
  const [Cardtype, setCardType] = useState(false);
  const [error, setError] = useState("");
  const [errorMessage, setErrorMessage] = useState("");
  const [currentPage, setCurrentPage] = useState(1);
  const [pagePerData, setPagePerData] = useState(10);

  const [showAlert, setShowAlert] = useState(false);
  const [alertTitle, setAlertTitle] = useState("");
  const [alertMessage, setAlertMessage] = useState("");
  const [alertType, setAlertType] = useState("");

  const handleShowSuccess = (resMessage) => {
    setAlertTitle("Alert");
    setAlertMessage(resMessage);
    setAlertType("blue");
    setShowAlert(true);
  };

  const handleShowError = (resMessage) => {
    setAlertTitle("Error");
    setAlertMessage(resMessage);
    setAlertType("red");
    setShowAlert(true);
  };

  const handleCloseAlert = () => {
    setShowAlert(false);
  };

  const showSuccess = (resMessage) => {
    Swal.fire({
      title: "Success",
      text: resMessage,
      allowOutsideClick: false,
      icon: "success",
      confirmButtonText: "OK",
    });
  };
  const showError = (resMessage) => {
    Swal.fire({
      text: resMessage,
      allowOutsideClick: false,
      icon: "error",
      title: "Oops...",
      confirmButtonText: "OK",
    });
  };

  const Linktypes = () => {
    if (showhide === "Card") {
      Carddetails();
    } else if (showhide === "Account") {
      AccountDeatils();
    }
  };
  const handlesshowhide = (event) => {
    const getuser = event.target.value;

    setShowhide(getuser);
  };

  // Dropdown Account Type
  useEffect(() => {
    Accounttype();
  }, []);

  const Accounttype = async () => {
    try {
      const response = await amsApi.post(
        `accountType/getACTypeListByParticiptWise`,
        {
          strParticipantId: participantID,
        },
        {
          headers: {
            "Content-Type": "application/json",
          },
        }
      );
      if (response.data.code === "S0000") {
        setType(response.data.accountTypeMasterlistData);
      } else {
        //  handleShowError(response.data.message);
      }
    } catch (error) {
      // handleShowError(error);
    }
  };

  // Dropdown Card Type
  useEffect(() => {
    Card();
  }, []);

  const Card = async () => {
    try {
      const response = await amsApi.post(
        `card_type/getCardTypedataForLinkage`,
        {
          strParticipantID: "6",
        },
        {
          headers: {
            "Content-Type": "application/json",
          },
        }
      );
      if (response.data.code === "S0000") {
        setCarddata(response.data.cardTypeList);
      } else {
        // handleShowError(response.data.message);
      }
    } catch (error) {
      // handleShowError(error);
    }
  };

  // Search Link Card Data
  const AccountDeatils = async (event) => {
    if (accounttype === "" || accountno === "") {
      setError(true);
      // showError("Please provide  Account Type and Account Number.");
    } else {
      try {
        const response = await amsApi.post(
          `card-account-linkage/getLinkagedata-base-on-account`,
          {
            strAccountType: accounttype,
            strAccountNumber: accountno,
          }
        );
        if (response.data.code === "S0000") {
          setData(response.data.cardAccountLinkagelist);
          setOpenAccountType(true);
          setCardType(false);
          // showSuccess(response.data.message);
        } else {
          handleShowError(response.data.message);
        }
      } catch (error) {
        handleShowError(
          "Sorry, the server is under mentaines. Please try again later"
        );
      }
    }
  };

  // Card type serach
  const Carddetails = async (e) => {
    if (cardtype === "" || cardno === "") {
      setError(true);
      // showError("Please provide both Card Type and Card Number.");
    } else {
      try {
        const response = await amsApi.post(
          `card-account-linkage/getLinkagedata-base-on-card`,
          {
            strCardType: cardtype,
            strCardNumber: cardno,
          }
        );
        if (response.data.code === "S0000") {
          setCdata(response.data.cardAccountLinkagelist);
          setCardType(true);
          setOpenAccountType(false);
          // showSuccess(response.data.message);
        } else {
          handleShowError(response.data.message);
        }
      } catch (error) {
        handleShowError(
          "Sorry, the server is under mentaines. Please try again later"
        );
      }
    }
  };
  // Paginations for charge related
  const [page, setPage] = useState("");
  const [frompage, setFromPage] = useState("");
  const [topage, setToPage] = useState("");

  const itemPerPageHandler = (e) => {
    setPage(e.target.value);
    if (e.target.value === "custom") {
      return null;
    }
    setPagePerData(parseInt(e.target.value));
    setCurrentPage(1);
  };

  const customItemPerPageHandler = () => {
    setPagePerData(parseInt(topage));
    setCurrentPage(frompage);
  };

  const TotalPageNo = Math
    .floor
    // parseInt(filteredData[0].tcount) / parseInt(pagePerData) + 1
    ();

  function nextpage() {
    if (TotalPageNo > currentPage) {
      setCurrentPage(currentPage + 1);
    }
  }
  function prevPage() {
    if (currentPage > 1) {
      setCurrentPage(currentPage - 1);
    }
  }
  return (
    <AppLayout>
      <div className="max-w-full mx-auto h-full">
        <div className="w-full shadow-md mt-2 ">
          <div className="px-8 sm:px-10 bg-blue-200 ">
            <div className=" flex  items-center justify-between ">
              <p className="text-base  sm:text-xm text-black :text-2xl  leading-normal ">
                Card Account Linkage View
              </p>
            </div>
          </div>
        </div>
        <div className=" md:col-span-2 lg:col-span-1  ">
          <form className="">
            <div className="overflow-hidden shadow ">
              <div className=" px-4 sm:p-2 bg-white  ">
                <div className="grid gap-4 grid-cols-3 px-4 ">
                  <div>
                    <label
                      htmlFor="text"
                      className="block text-xs font-semibold text-gray-700"
                    >
                      Search By
                    </label>
                    <select
                      name="accountType"
                      onChange={(e) => handlesshowhide(e)}
                      value={showhide}
                      className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      required
                    >
                      <option value="">select </option>
                      <option value="Card">Card</option>
                      <option value="Account">Account</option>
                    </select>
                  </div>
                  {showhide === "Card" && (
                    <div
                      className="grid gap-4 grid-cols-2 col-span-2"
                      id="card"
                    >
                      <div>
                        <label
                          htmlFor="text"
                          className="block text-xs font-semibold text-gray-700"
                        >
                          Card Type{""}
                          <span className="text-red-600 px-2">*</span>
                        </label>
                        <select
                          id="accountType"
                          name="accountType"
                          value={cardtype}
                          onChange={(e) => setCardtype(e.target.value)}
                          className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                          required
                        >
                          <option value="">Select</option>
                          {carddata.map((data) => (
                            // <optgroup key={uuidv4()}>
                            <option value={data.strCardType}>
                              {data.strCardType}
                              {data.strdescription}
                            </option>
                            // </optgroup>
                          ))}
                        </select>
                        {error && cardtype.length <= 0 ? (
                          <p className="text-red-500   text-xm font-normal">
                            Please Select Card Type
                          </p>
                        ) : (
                          ""
                        )}
                      </div>
                      <div>
                        <label
                          htmlFor="text"
                          className="block text-xs font-semibold text-gray-700"
                        >
                          Card Number{""}
                          <span className="text-red-600 px-2">*</span>
                        </label>
                        <input
                          type="text"
                          value={cardno}
                          // onChange={(e) => setCardno(e.target.value)}
                          onChange={(e) => {
                            const value = e.target.value;
                            setCardno(value);

                            const regex = /^[0-9]*$/;
                            if (!regex.test(value)) {
                              setErrorMessage(
                                " Only allow numeric digits"
                                // "Please enter only numeric values"
                              );
                            } else {
                              setErrorMessage("");
                            }
                            setError("");
                          }}
                          onBlur={() => {
                            if (cardno.length === 0) {
                              setError("Please Enter Card number!");
                              setErrorMessage("");
                            }
                          }}
                          className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                          placeholder="Enter Card number"
                        />
                        {errorMessage ? (
                          <p className="text-red-500 text-xm font-normal">
                            {errorMessage}
                          </p>
                        ) : error && cardno.length <= 0 ? (
                          <p className="text-red-500 text-xm font-normal">
                            Please Enter Card number!
                          </p>
                        ) : null}
                        {/* {error && cardno.length <= 0 ? (
                            <p className="text-red-500   text-sm font-medium">
                              Please Enter Card number
                            </p>
                          ) : (
                            "-"
                          )} */}
                      </div>
                    </div>
                  )}
                  {showhide === "Account" && (
                    <div
                      id="account"
                      className="grid gap-4 grid-cols-2 col-span-2"
                    >
                      <div>
                        <label
                          htmlFor="text"
                          className="block text-xs font-semibold text-gray-700"
                        >
                          Account Type{""}
                          <span className="text-red-600 px-2">*</span>
                        </label>
                        <select
                          id="accountType"
                          name="accountType"
                          value={accounttype}
                          onChange={(e) => setAccountType(e.target.value)}
                          className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                          required
                        >
                          <option value="">Select</option>
                          {type.map((data) => (
                            // <optgroup key={uuidv4()}>
                            <option value={data.strAccountType}>
                              {data.strAccountType || "-"} -
                              {data.strDescription || "-"}
                            </option>
                            // </optgroup>
                          ))}
                        </select>
                        {error && accounttype.length <= 0 ? (
                          <p className="text-red-500   text-sm font-normal">
                            Please Select Account Type
                          </p>
                        ) : (
                          ""
                        )}
                      </div>
                      <div>
                        <label
                          htmlFor="text"
                          className="block text-xs font-semibold text-gray-700"
                        >
                          Account Number{""}
                          <span className="text-red-600 px-2">*</span>
                        </label>
                        <input
                          type="text"
                          value={accountno}
                          // onChange={(e) => setAccountno(e.target.value)}
                          onChange={(e) => {
                            const value = e.target.value;
                            setAccountno(value);

                            const regex = /^[0-9]*$/;
                            if (!regex.test(value)) {
                              setErrorMessage(
                                " Only allow numeric digits"
                                // "Please enter only numeric values"
                              );
                            } else {
                              setErrorMessage("");
                            }
                            setError("");
                          }}
                          onBlur={() => {
                            if (accountno.length === 0) {
                              setError("Please Enter Account number!");
                              setErrorMessage("");
                            }
                          }}
                          className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                          placeholder=" Enter Account number"
                        />
                        {errorMessage ? (
                          <p className="text-red-500 text-xs font-normal">
                            {errorMessage}
                          </p>
                        ) : error && accountno.length <= 0 ? (
                          <p className="text-red-500 text-xs font-normal">
                            Please Enter Account number!
                          </p>
                        ) : null}
                        {/* {error && accountno.length <= 0 ? (
                            <p className="text-red-500   text-sm font-medium">
                              Please Enter Account number
                            </p>
                          ) : (
                            "-"
                          )} */}
                      </div>
                    </div>
                  )}
                </div>
              </div>
              <div className="bg-gray-100 px-4 py-2 text-right sm:px-2 ">
                <button
                  title="Click Search Button"
                  type="button"
                  onClick={Linktypes}
                  data-modal-toggle="defaultModal"
                  className="bg-blue-500 hover:bg-blue-700 focus:ring-2 focus:ring-blue-800 focus:ring-offset-2 text-white font-bold py-2 px-4 border border-blue-700 rounded"
                >
                  <MagnifyingGlassIcon className="h-3 w-3" />
                </button>
              </div>
            </div>
          </form>
        </div>

        {/* Table */}
        {openAccountType && (
          <div className="bg-white overflow-x-auto relative shadow-md sm:rounded-lg mt-10">
            <div className="bg-blue-100 px-2 flex items-center justify-between  sm:px-6">
              <div className="flex-1 flex justify-between sm:hidden">
                <button
                  onClick={prevPage}
                  className="relative inline-flex items-center px-4 py-2 border border-gray-300 text-sm font-medium rounded-md text-gray-700 bg-white hover:bg-gray-50"
                >
                  Previous
                </button>
                <button
                  onClick={nextpage}
                  className="ml-3 relative inline-flex items-center px-4 py-2 border border-gray-300 text-sm font-medium rounded-md text-gray-700 bg-white hover:bg-gray-50"
                >
                  Next
                </button>
              </div>
              <div className="  sm:flex-1 sm:flex sm:items-center sm:justify-between">
                <div className="mx-2">
                  <label htmlFor="" className="text-sm text-gray-700">
                    Items Per Page
                  </label>
                  <select
                    onChange={itemPerPageHandler}
                    onClick={AccountDeatils}
                    value={page}
                    className="focus:ring-indigo-500 focus:border-indigo-500 mx-2 h-full py-0 px-4 border border-gray-900 bg-transparent text-gray-900 text-sm rounded-md"
                  >
                    <option value="10">10</option>
                    <option value="25">25</option>
                    <option value="50">50</option>
                    <option value="75">75</option>
                    <option value="100">100</option>
                    <option value="custom">{"Custom"}</option>
                  </select>
                </div>
                <div className="lg:mr-50">
                  {page === "custom" && (
                    <>
                      <div className="grid lg:grid-cols-3 md:grid-cols-3 sm:grid-cols-3 gap-4">
                        <div className="">
                          <input
                            type="text"
                            name="custom"
                            id="custom"
                            autoComplete="custom"
                            value={frompage}
                            onChange={(e) => setFromPage(e.target.value)}
                            className="border border-black text-black text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-black dark:border-black dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
                            placeholder="From Page"
                            required
                          />
                        </div>
                        <div className="">
                          <input
                            type="text"
                            name="custom"
                            id="custom"
                            autoComplete="custom"
                            value={topage}
                            onChange={(e) => setToPage(e.target.value)}
                            className="border border-black text-black text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-black dark:border-black dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
                            placeholder="To Page"
                            required
                          />
                        </div>
                        <div className="">
                          <button
                            type="submit"
                            onClick={AccountDeatils}
                            className="inline-flex justify-right whitespace-nowrap py-1 px-8 border border-transparent shadow-sm text-xs font-medium rounded-md text-white bg-blue-600 hover:bg-blue-400 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-400"
                          >
                            Submit
                          </button>
                        </div>
                      </div>
                    </>
                  )}
                </div>
                <div>
                  <div
                    className="relative z-0 inline-flex rounded-md shadow-sm   items-center -space-x-px"
                    aria-label="Pagination"
                  >
                    <div>
                      <p className="text-sm text-gray-900  mx-3">
                        Showing
                        <span className="font-bold  mx-2 text-lg text-blue-700 ">
                          {currentPage}
                        </span>
                        <span>of</span>
                        <span className="font-bold mx-2 text-lg text-blue-700">
                          {/* {TotalPageNo} */}
                          {pagePerData}
                        </span>
                        Results
                      </p>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <div className="overflow-x-auto relative shadow-md  ">
              <table className="w-full text-xs text-left">
                <thead className="text-xs uppercase bg-gray-100">
                  <tr>
                    <th scope="col" className="py-2 px-6">
                      CARD NUMBER
                    </th>
                    <th scope="col" className="py-2 px-6">
                      ACCOUNT NUMBER
                    </th>
                  </tr>
                </thead>
                <tbody>
                  <>
                    {Accountdata.map(({ strAccountNumber, strCardNumber }) => (
                      <tr
                        key={uuidv4()}
                        className="bg-blue-00 border-b  hover:bg-blue-200 border-blue-400"
                      >
                        <td className="px-6 py-2 whitespace-nowrap">
                          <span className="text-sm font-normal text-gray-900">
                            {strCardNumber || "-"}
                          </span>
                        </td>
                        <td className="px-6 py-2 whitespace-nowrap">
                          <span className="text-sm font-normal text-gray-900">
                            {strAccountNumber || "-"}
                          </span>
                        </td>
                      </tr>
                    ))}
                  </>
                </tbody>
              </table>
            </div>
          </div>
        )}

        {/* card  */}
        {Cardtype && (
          <div className="bg-white overflow-x-auto relative shadow-md sm:rounded-lg mt-10">
            <div className="overflow-x-auto relative shadow-md  ">
              <table className="w-full text-xs text-left">
                <thead className="text-xs uppercase bg-gray-100">
                  <tr>
                    <th scope="col" className="py-2 px-6">
                      CARD NUMBER
                    </th>
                    <th scope="col" className="py-2 px-6">
                      ACCOUNT NUMBER
                    </th>
                  </tr>
                </thead>
                <tbody>
                  <>
                    {Cdata.map(({ strAccountNumber, strCardNumber }) => (
                      <tr
                        key={uuidv4()}
                        className="bg-blue-00 border-b  hover:bg-blue-200 border-blue-400"
                      >
                        <td className="px-6 py-2 whitespace-nowrap">
                          <span className="text-sm font-normal text-gray-900">
                            {""}
                            {strCardNumber}
                          </span>
                        </td>
                        <td className="px-6 py-4 whitespace-nowrap">
                          <span className="text-sm font-normal text-gray-900">
                            {strAccountNumber}
                          </span>
                        </td>
                      </tr>
                    ))}
                  </>
                </tbody>
              </table>
            </div>
          </div>
        )}
      </div>

      {/* Your existing code here */}

      {showAlert && (
        <CustomAlert
          title={alertTitle}
          message={alertMessage}
          type={alertType}
          onClose={handleCloseAlert}
        />
      )}
      {/* </div> */}
    </AppLayout>
  );
}
