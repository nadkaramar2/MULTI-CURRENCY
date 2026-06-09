import React, { useState, useEffect } from "react";
import AppLayout from "../../layout/AppLayout";
import Swal from "sweetalert2";
import { v4 as uuidv4 } from "uuid";
import amsApi from "../../api/amsApi";
import { useParams, useNavigate } from "react-router-dom";
import CustomAlert from "../../layout/CustomAlert";

export default function AccountStatmet() {
  const navigate = useNavigate();
  const [fetchdata, setFetchData] = useState([]);
  const { fromdate, todate } = useParams();
  const { selectedRowData } = useParams();
  let custid = localStorage.getItem("custid");
  let FirstName = localStorage.getItem("Firstname");
  let LastName = localStorage.getItem("LastName");
  const [searchQuery, setSearchQuery] = useState("");
  const [accountType, accountNumber] = selectedRowData.split("");

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

  useEffect(() => {
    AccountStatm();
  }, []);

  const AccountStatm = async () => {
    try {
      const response = await amsApi.post(
        `account-statement/getAccountStatement`,
        {
          strAccountType: accountType,
          strAccountNumber: accountNumber,
          fromDate: fromdate,
          toDate: todate,
        },

        {
          headers: {
            "Content-Type": "application/json",
          },
        }
      );
      if (response.data.code === "S0000") {
        setFetchData(response.data.accountStatementList);
        // showSuccess(response.data.message);
      } else {
        handleShowError(response.data.message);
      }
    } catch (error) {
      handleShowError(
        "Sorry, the server is under mentaines. Please try again later"
      );
    }
  };
  return (
    <AppLayout>
      <div className="max-w-7xl mx-auto h-full">
        <div className="w-full shadow-md mt-2 bg-blue-600">
          <div className="px-8 sm:px-10 rounded-t-lg ">
            <div className=" flex  items-center justify-between ">
              <p className="text-base sm:text-lg md:text-xl text-white lg:text-2xl font-bold leading-normal">
                Customer Basic Information
              </p>
            </div>
          </div>
        </div>
        <div className=" md:col-span-2 lg:col-span-1  bg-blue-200">
          <form className="">
            <div className="overflow-hidden shadow sm:rounded-md">
              <div className=" px-4 py-2 sm:p-2 bg-blue-200  ">
                <div className="grid gap-6 mb-2 md:grid-cols-3 px-8">
                  <div>
                    <label
                      htmlFor="GL Account Type"
                      className="block text-sm font-semibold text-gray-700"
                    >
                      Customer Id
                    </label>
                    <input
                      type="text"
                      defaultValue={custid}
                      className="block w-full px-3 py-1.5 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      placeholder="Enter Customer id"
                    />
                  </div>
                  <div>
                    <div>
                      <label
                        htmlFor="GL Account Description"
                        className="block text-sm font-semibold text-gray-700"
                      >
                        Customer Name
                      </label>
                      <input
                        type="text"
                        className="block w-full px-3 py-1.5 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                        placeholder="Enter Customer name"
                        defaultValue={
                          FirstName && LastName
                            ? FirstName + " " + LastName
                            : ""
                        }
                      />
                    </div>
                  </div>

                  <div className="py-4">
                    <button
                      title="Go Back"
                      type="button"
                      onClick={() => navigate(-1)}
                      className="text-white bg-blue-500 focus:outline-none focus:ring-4 focus:ring-blue-300 font-medium rounded-lg text-sm px-5 py-2.5 mr-2 mb-2 dark:bg-gray-800 dark:hover:bg-gray-700 dark:focus:ring-gray-700 dark:border-gray-700"
                    >
                      BACK
                    </button>
                  </div>
                </div>
              </div>
            </div>
          </form>
        </div>
        {/* table */}
        <div className="bg-white overflow-x-auto relative shadow-md sm:rounded-lg  ">
          <div className="overflow-x-auto relative shadow-md  ">
            <div className="bg-white overflow-x-auto relative shadow-md  mt-2">
              <div className="overflow-x-auto relative shadow-md  ">
                <h2 className="font-normal md:font-bold">Account Statement</h2>
              </div>
            </div>

            <div className="flex justify-end items-center sm:px-6 lg:px-8 bg-blue-200  mt-0 rounded-t-lg ">
              <div className=" flex justify-end   rounded-md border border-transparent  px-5 mx-4 text-sm  font-medium text-white ">
                <div className="pt-2 relative  mx-auto text-gray-600">
                  <input
                    title="Search Data"
                    type="search"
                    id="search"
                    value={searchQuery}
                    onChange={(e) => setSearchQuery(e.target.value)}
                    className="block w-full px-2 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                    placeholder="Search.."
                  />
                  <button
                    type="submit"
                    className="absolute right-0 top-0 mt-4 mr-2  "
                  >
                    <svg
                      xmlns="http://www.w3.org/2000/svg"
                      fill="none"
                      viewBox="0 0 24 24"
                      strokeWidth={1.5}
                      stroke="currentColor"
                      className="w-6 h-6"
                    >
                      <path
                        strokeLinecap="round"
                        strokeLinejoin="round"
                        d="M21 21l-5.197-5.197m0 0A7.5 7.5 0 105.196 5.196a7.5 7.5 0 0010.607 10.607z"
                      />
                    </svg>
                  </button>
                </div>
              </div>
            </div>

            <div className="bg-white overflow-x-auto relative shadow-md ">
              <div className="table-wrp block max-h-[27rem] ">
                <table className="w-full text-sm text-left text-clack dark:text-blue-100">
                  <thead className="text-xs border-b sticky top-0 text-black uppercase bg-gray-100 dark:text-white">
                    <tr>
                      <th scope="col" className="py-4 px-6">
                        TXN DATE
                      </th>
                      <th scope="col" className="py-4 px-6">
                        TXN ID
                      </th>
                      <th scope="col" className="py-4 px-6">
                        TXN TYPE
                      </th>
                      <th scope="col" className="py-4 px-6">
                        TXN AMOUNT
                      </th>
                      <th scope="col" className="py-4 px-6">
                        CURRENT BALANCE
                      </th>
                      <th scope="col" className="py-4 px-6">
                        TXN DESCRIPTION
                      </th>
                    </tr>
                  </thead>
                  <tbody>
                    {fetchdata.length > 0 ? (
                      <>
                        {fetchdata.map((value) => (
                          <tr
                            key={uuidv4()}
                            className="bg-blue-00 border-b  hover:bg-blue-200 border-blue-400"
                          >
                            <td className="px-6 py-4 whitespace-nowrap">
                              <span className="text-sm font-medium text-gray-900">
                                {value.strTransactionDate || "-"}
                              </span>
                            </td>
                            <td className="px-6 py-4 whitespace-nowrap">
                              <span className="text-sm font-medium text-gray-900">
                                {value.strTransactionID || "-"}
                              </span>
                            </td>
                            <td className="px-6 py-4 whitespace-nowrap">
                              <span className="text-sm font-medium text-gray-900">
                                {value.strTranType || "-"}
                              </span>
                            </td>
                            <td className="px-6 py-4 whitespace-nowrap">
                              <span className="text-sm font-medium text-gray-900">
                                {value.strTransactionAmount || "-"}
                              </span>
                            </td>
                            <td className="px-6 py-4 whitespace-nowrap">
                              <span className="text-sm font-medium text-gray-900">
                                {value.strClosingBalance || "-"}
                              </span>
                            </td>
                            <td className="px-6 py-4 whitespace-nowrap">
                              <span className="text-sm font-medium text-gray-900">
                                {value.strTransactionDetails || "-"}
                              </span>
                            </td>
                          </tr>
                        ))}
                      </>
                    ) : (
                      <tr>
                        <td colSpan="6" className="px-6 py-4 text-center">
                          <span className="text-lg font-medium text-gray-500">
                            No data available.
                          </span>
                        </td>
                      </tr>
                    )}
                  </tbody>
                </table>
              </div>
            </div>
          </div>
        </div>
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
