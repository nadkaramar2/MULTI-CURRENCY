import React, { useState, useEffect } from "react";
import AppLayout from "../layout/AppLayout";
import amsApi from "../api/amsApi";
import Swal from "sweetalert2";
import { ArrowLeftIcon } from "@heroicons/react/24/solid";
import { useNavigate } from "react-router-dom";
import { v4 as uuidv4 } from "uuid";
import { NavLink } from "react-router-dom";
import CustomAlert from "../layout/CustomAlert";

export default function AuthorizeDormancy() {
  const [data, setData] = useState([]);
  const [searchQuery, setSearchQuery] = useState("");
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
    CheckerUserList();
  }, []);

  const CheckerUserList = async () => {
    // if (accountno === "" || accountno.length === 0) {
    //   setError(true);
    // } else {
    try {
      const response = await amsApi.get(
        `dormantToActive/getPendingCheckerUserList`,
        {},
        {
          headers: {
            "Content-Type": "application/json",
          },
        }
      );
      if (response.data.code === "S0000") {
        // showSuccess(response.data.message);
        setData(response.data.dormantToActiveMasters);

        // setError("");
        // Clear();
      } else {
        handleShowError(response.data);
      }
    } catch (error) {
      handleShowError(error.response.data);
    }
    // }
  };

  // sorted tabled data
  const [sortOrder, setSortOrder] = useState("asc");
  const [sortColumn, setSortColumn] = useState("");
  const handleSort = (column) => {
    if (column === sortColumn) {
      setSortOrder(sortOrder === "asc" ? "desc" : "asc");
    } else {
      setSortColumn(column);
      setSortOrder("asc");
    }
  };

  const sortedData = data.slice(0).sort((a, b) => {
    const dateA = new Date(a.requestDate);
    const dateB = new Date(b.requestDate);
    const timeA = new Date("1970/01/01 " + a.requestTime);
    const timeB = new Date("1970/01/01 " + b.requestTime);

    if (dateA.getTime() === dateB.getTime()) {
      return sortOrder === "asc" ? timeA - timeB : timeB - timeA;
    } else {
      return sortOrder === "asc" ? dateA - dateB : dateB - dateA;
    }
  });

  const renderSortArrow = (column) => {
    const arrowStyle = {
      cursor: "pointer",
      transition: "transform 0.2s",
    };

    return (
      <span className="cursor-pointer   text-md hover:bg-blue-500  hover:text-white px-1 mx-1">
        {sortColumn === column ? (sortOrder === "asc" ? "▲" : "▼") : "⇅"}
      </span>
    );
  };

  console.log(sortOrder);
  console.log(sortedData);

  return (
    <AppLayout>
      <div className="max-w-7xl mx-auto h-full">
        <div className="w-full shadow-md mt-2 ">
          <div className="px-4 sm:px-10 bg-blue-200">
            <div className=" flex  items-center justify-between">
              <p className="text-base  sm:text-xm text-black :text-2xl  leading-normal">
                Authorize Dormancy
              </p>
              {/* <div className=" flex justify-center  rounded-md border border-transparent  px-5 mx-4 text-sm  font-medium text-white  ">
                <div className="pt-2 relative  mx-auto text-gray-600">
                  <input
                    title="Search Data"
                    type="search"
                    id="search"
                    // value={searchQuery}
                    // onChange={(e) => setSearchQuery(e.target.value)}
                    className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                    placeholder="Search.."
                  />
                  <button
                    type="submit"
                    className="absolute right-0 top-0 mt-4 px-4  "
                  >
                    <svg
                      xmlns="http://www.w3.org/2000/svg"
                      fill="none"
                      viewBox="0 0 24 24"
                      strokeWidth={1.5}
                      stroke="currentColor"
                      className="w-6 h-6 "
                    >
                      <path
                        strokeLinecap="round"
                        strokeLinejoin="round"
                        d="M21 21l-5.197-5.197m0 0A7.5 7.5 0 105.196 5.196a7.5 7.5 0 0010.607 10.607z"
                      />
                    </svg>
                  </button>
                </div>
              </div> */}
            </div>
          </div>
        </div>
        {/* <div className="flex justify-end items-center sm:px-2 lg:px-2 bg-blue-100 mt-1 ">
          <div className=" flex justify-center  rounded-md border border-transparent  px-5 mx-4 text-sm  font-medium text-white  ">
            <div className="pt-2 relative  mx-auto text-gray-600">
              <input
                title="Search Data"
                type="search"
                id="search"
                value={searchQuery}
                onChange={(e) => setSearchQuery(e.target.value)}
                className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                placeholder="Search.."
              />
              <button
                type="submit"
                className="absolute right-0 top-0 mt-4 px-4  "
              >
                <svg
                  xmlns="http://www.w3.org/2000/svg"
                  fill="none"
                  viewBox="0 0 24 24"
                  strokeWidth={1.5}
                  stroke="currentColor"
                  className="w-6 h-6 "
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
        </div> */}
        <div className="bg-white overflow-x-auto relative shadow-md mt-2 ">
          <div className="table-wrp block max-h-[27rem] max-w-[10rem] ">
            {/* <div ref={conponentPDF} style={{ width: "100%" }}> */}
            <table className="w-full text-sm text-left text-black dark:text-blue-100">
              <thead className="text-xs border-b sticky top-0 text-gray-500 uppercase bg-gray-100 dark:text-white">
                <th
                  scope="col"
                  className="py-2.5 px-9 whitespace-nowrap"
                  onClick={() => handleSort("requestDate")}
                >
                  Date of Request {renderSortArrow("requestDate")}
                </th>

                <th
                  scope="col"
                  className="py-2.5 px-9 whitespace-nowrap"
                  onClick={() => handleSort("requestTime")}
                >
                  Time of Request {renderSortArrow("requestTime")}
                </th>

                <th
                  scope="col"
                  className="py-2.5 px-9 whitespace-nowrap"
                  onClick={() => handleSort("accountNumber")}
                >
                  Account number {renderSortArrow("accountNumber")}
                </th>

                <th
                  scope="col"
                  className="py-2.5 px-9 whitespace-nowrap"
                  onClick={() => handleSort("accountType")}
                >
                  Account Type {renderSortArrow("accountType")}
                </th>

                <th
                  scope="col"
                  className="py-2.5 px-9 whitespace-nowrap"
                  onClick={() => handleSort("dormantMarkedDate")}
                >
                  Dormant Marked Date {renderSortArrow("dormantMarkedDate")}
                </th>
                <th
                  scope="col"
                  className="py-2.5 px-9 whitespace-nowrap"
                  onClick={() => handleSort("requestRaisedBy")}
                >
                  Request Raised by {renderSortArrow("requestRaisedBy")}
                </th>
                <th
                  scope="col"
                  className="py-2.5 px-9 whitespace-nowrap"
                  onClick={() => handleSort("reasonForActive")}
                >
                  Reason for Active {renderSortArrow("reasonForActive")}
                </th>
              </thead>

              <tbody>
                {sortedData.length > 0 ? (
                  <>
                    {sortedData.map((value) => (
                      <tr
                        key={uuidv4()}
                        className="border-b dark:border-neutral-500"
                      >
                        <td className="whitespace-nowrap px-9 py-2.5">
                          <span className="text-sm font-medium text-gray-900">
                            {value.requestDate || "-"}
                          </span>
                        </td>

                        <td className="px-9 py-2.5 whitespace-nowrap">
                          <span className="text-sm font-medium text-gray-900">
                            {value.requestTime || "-"}
                          </span>
                        </td>

                        <td className="px-9 py-2.5 whitespace-nowrap">
                          <span className="text-sm font-medium text-gray-900">
                            {/* {value.accountNumber || "-"} */}
                            <NavLink
                              to={`/authorizeview/${value.accountNumber}`}
                              className="border-blue-300 shadow-md p-2  text-blue-700   rounded-full focus:ring-blue-400"
                            >
                              {value.accountNumber || "-"}
                            </NavLink>
                          </span>
                        </td>

                        <td className="px-9 py-4 text-center whitespace-nowrap">
                          <span className="text-sm font-medium text-gray-900">
                            {value.accountType || "-"}
                          </span>
                        </td>

                        <td className="px-9 py-4 text-center whitespace-nowrap">
                          <span className="text-sm font-medium text-gray-900">
                            {value.dormantMarkedDate || "-"}
                          </span>
                        </td>
                        <td className="px-9 py-4 text-center whitespace-nowrap">
                          <span className="text-sm font-medium text-gray-900">
                            {value.requestRaisedBy || "-"}
                          </span>
                        </td>
                        <td className="px-9 py-4 text-center whitespace-nowrap">
                          <span className="text-sm font-medium text-gray-900">
                            {value.reasonForActive || "-"}
                          </span>
                        </td>
                      </tr>
                    ))}
                  </>
                ) : (
                  <tr>
                    <td colSpan="6" className="px-9 py-2 text-center">
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
