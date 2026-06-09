import React, { useState, useEffect } from "react";
import AppLayout from "../../layout/AppLayout";
import amsApi from "../../api/amsApi";
import CustomAlert from "../../layout/CustomAlert";
import { v4 as uuidv4 } from "uuid";
import AutoPagintation from "../../UI/AutoPagintation";
export default function Currencygstreports() {
  const [searchQuery, setSearchQuery] = useState("");
  const [accounttype, setAccountType] = useState("");
  const [typedata, setTypedata] = useState([]);
  const [fetchData, setFetchData] = useState([]);
  const storedToken = localStorage.getItem("token");

  const [error, setError] = useState("");
  //   const [itemPage, setItemPage] = useState(10);
  //   const [tcount, setTcount] = useState(1);
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
  // DATE AND TIME
  var currentdate = new Date();
  var time =
    currentdate.getHours() +
    ":" +
    currentdate.getMinutes() +
    ":" +
    currentdate.getSeconds();

  var date =
    currentdate.getDate() +
    "/" +
    (currentdate.getMonth() + 1) +
    "/" +
    currentdate.getFullYear();

  useEffect(() => {
    gltype();
  }, []);

  const gltype = async () => {
    try {
      const response = await amsApi.post(
        `multiCurrencyFeeType_Master/getGSTType`,
        {},
        {
          headers: {
            "Content-Type": "application/json",
          },
        }
      );
      if (response.data.code === "S0000") {
        setTypedata(response.data.getData);
      } else {
        // handleShowError(response.data.message);
      }
    } catch (error) {
      // handleShowError(error.response.data.message);
    }
  };

  useEffect(() => {
    FeesCollectedDeatils();
  }, [accounttype]);

  const FeesCollectedDeatils = async () => {
    if (accounttype === "" || accounttype.length === 0) {
      setError("Please select Account Type");
    } else {
      try {
        const response = await amsApi.post(
          `multiCurrencyFeeType_Master/ViewGst`,
          { strGLAccountType: accounttype },
          {
            headers: {
              "Content-Type": "application/json",
            },
          }
        );
        if (response.data.code === "S0000") {
          setFetchData(response.data.glAccountData);
        } else {
          handleShowError(response.data.message);
        }
      } catch (error) {
        handleShowError(error.response.data.message);
      }
    }
  };
  // sorted tabled data
  const [sortColumn, setSortColumn] = useState(""); // State to track the sorted column
  const [sortOrder, setSortOrder] = useState("asc"); // State to track sorting order
  const handleSort = (column) => {
    if (column === sortColumn) {
      // If clicking on the same column, toggle the sorting order
      setSortOrder(sortOrder === "asc" ? "desc" : "asc");
    } else {
      // If clicking on a different column, set the new column and default to ascending order
      setSortColumn(column);
      setSortOrder("asc");
    }
  };
  const renderSortArrow = (column) => {
    if (column === sortColumn) {
      return sortOrder === "asc" ? "↑" : "↓";
    }
    return "↕";
  };
  // Sort the data based on the chosen column and order
  const sortedData = [...fetchData].sort((a, b) => {
    if (sortOrder === "asc") {
      const aValue = a[sortColumn] || "";
      const bValue = b[sortColumn] || "";
      return aValue.localeCompare(bValue);
    } else {
      const aValue = a[sortColumn] || "";
      const bValue = b[sortColumn] || "";
      return bValue.localeCompare(aValue);
    }
  });

  return (
    <AppLayout>
      <div className="max-w-full mx-auto h-full scale-96">
        <div className="w-full shadow-md mt-2">
          <div className="px-4  sm:px-10  bg-blue-200">
            <div className=" flex  items-center justify-between">
              <p className="text-base  sm:text-xm text-black :text-2xl  leading-normal ">
                Multi Currency GST Reports
              </p>
            </div>
          </div>
        </div>
        <div className=" md:col-span-2 lg:col-span-1  ">
          <div className="overflow-hidden shadow ">
            <div className=" px-2  sm:p-2 bg-white  ">
              <div className="grid gap-4  md:grid-cols-4  px-5">
                <div>
                  <label
                    htmlFor="text"
                    className="block  text-xs font-semibold text-gray-700 dark:text-white"
                  >
                    GL Account Type <span className="text-red-600 px-2">*</span>
                  </label>

                  <select
                    id="accountType"
                    name="accountType"
                    value={accounttype}
                    onChange={(e) => setAccountType(e.target.value)}
                    className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0 "
                  >
                    <option value="">Select</option>

                    {typedata.map((data) => (
                      <option value={data.gstType}>{data.gstType}</option>
                    ))}
                  </select>
                </div>
                {/* <div>
                  <div className=" w-full px-1 py-1 text-base font-normal text-gray-700 ">
                    Balance as on {date} {time}
                  </div>
                </div> */}
              </div>
            </div>
          </div>
        </div>

        <div className="bg-blue-100 px-4  flex items-center justify-end  sm:px-4">
          <div className="flex justify-center rounded-md border border-transparent px-2  text-sm font-medium text-white ">
            <div class=" relative mx-auto text-gray-600">
              <input
                title="Search Data"
                type="search"
                id="search"
                value={searchQuery}
                onChange={(e) => setSearchQuery(e.target.value)}
                className="block w-full px-2 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                placeholder="Search.."
              />
              <button type="submit" className="absolute right-0 top-0 mt-1 ">
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

        {/* <AutoPagintation
          addPageperData={setItemPage}
          addCurrentPage={setTcount}
          pagePerData={itemPage}
          currentPage={tcount}
          tcount={fetchData[0]?.strTotalCount}
          reCallApi={FeesCollectedDeatils}
        /> */}
        {/* Table */}
        <div className="overflow-x-auto relative shadow-md  ">
          <>
            <div className="table-wrap block  max-h-[27rem] max-w-[27rem]">
              <table className="w-full text-xs text-center text-clack dark:text-blue-100">
                <thead className="text-xs border-b sticky top-0 text-gray-400 uppercase bg-gray-100 dark:text-white">
                  <th
                    scope="col"
                    className="py-1.5 px-12 whitespace-nowrap"
                    onClick={() => handleSort("strTxnId")}
                  >
                    Txn Id {renderSortArrow("strTxnId")}
                  </th>
                  <th
                    scope="col"
                    className="py-1.5 px-12 whitespace-nowrap"
                    onClick={() => handleSort("transactionDate")}
                  >
                    transaction Date {renderSortArrow("transactionDate")}
                  </th>
                  <th
                    scope="col"
                    className="py-1.5 px-12 whitespace-nowrap"
                    onClick={() => handleSort("strTranType")}
                  >
                    Tran Type {renderSortArrow("strTranType")}
                  </th>

                  <th
                    scope="col"
                    className="py-1.5 px-12 whitespace-nowrap"
                    onClick={() => handleSort("strGLAccountType")}
                  >
                    GL Account Type {renderSortArrow("strGLAccountType")}
                  </th>

                  <th
                    scope="col"
                    className="py-1.5 px-12 whitespace-nowrap"
                    onClick={() => handleSort("strAccountNumber")}
                  >
                    Account Number {renderSortArrow("strAccountNumber")}
                  </th>
                  <th
                    scope="col"
                    className="py-1.5 px-12  text-right  whitespace-nowrap "
                    onClick={() => handleSort("strAmount")}
                  >
                    Amount {renderSortArrow("strAmount")}
                  </th>

                  <th
                    scope="col"
                    className="py-1.5 px-12 whitespace-nowrap"
                    onClick={() => handleSort("strTranMode")}
                  >
                    Tran Mode {renderSortArrow("strTranMode")}
                  </th>
                  <th
                    scope="col"
                    className="py-1.5 px-12 whitespace-nowrap"
                    onClick={() => handleSort("createdDate")}
                  >
                    created Date {renderSortArrow("createdDate")}
                  </th>
                  <th
                    scope="col"
                    className="py-1.5 px-12 whitespace-nowrap"
                    onClick={() => handleSort("strRef")}
                  >
                    Ref {renderSortArrow("strRef")}
                  </th>
                  <th
                    scope="col"
                    className="py-1.5 px-12  text-right  whitespace-nowrap "
                    onClick={() => handleSort("strClosingBalance")}
                  >
                    Closing Balance {renderSortArrow("strClosingBalance")}
                  </th>
                  <th
                    scope="col"
                    className="py-1.5 px-12 whitespace-nowrap"
                    onClick={() => handleSort("strCreated_by")}
                  >
                    Created_by {renderSortArrow("strCreated_by")}
                  </th>

                  {/* <th
                    scope="col"
                    className="py-1.5 px-12 whitespace-nowrap"
                    onClick={() => handleSort("strTotalCount")}
                  >
                    TotalCount {renderSortArrow("strTotalCount")}
                  </th> */}
                </thead>
                <tbody>
                  {sortedData
                    .filter(
                      (data) =>
                        data.strTxnId
                          .toLowerCase()
                          .includes(searchQuery.toLowerCase()) ||
                        data.strTranType
                          .toLowerCase()
                          .includes(searchQuery.toLowerCase()) ||
                        data.strGLAccountType
                          .toLowerCase()
                          .includes(searchQuery.toLowerCase()) ||
                        data.strAccountNumber
                          .toLowerCase()
                          .includes(searchQuery.toLowerCase())
                    )
                    .map((data) => (
                      <tr
                        key={uuidv4()}
                        className=" border-b dark:border-neutral-500"
                      >
                        <td className="px-12 py-1.5 whitespace-nowrap">
                          <div className="text-sm text-gray-900">
                            {data.strTxnId || "-"}
                          </div>
                        </td>
                        <td className="px-12 py-1.5 whitespace-nowrap">
                          <div className="text-sm text-gray-900">
                            {data.transactionDate || "-"}
                          </div>
                        </td>
                        <td className="px-12 py-1.5 whitespace-nowrap">
                          <div className="text-sm text-gray-900">
                            {data.strTranType || "-"}
                          </div>
                        </td>
                        <td className="px-12 py-1.5 whitespace-nowrap">
                          <div className="text-sm text-gray-900">
                            {data.strGLAccountType || "-"}
                          </div>
                        </td>
                        <td className="px-12 py-1.5 whitespace-nowrap">
                          <div className="text-sm text-gray-900">
                            {data.strAccountNumber || "-"}
                          </div>
                        </td>
                        <td className="  px-11 py-1.5 text-right whitespace-nowrap">
                          <div className="text-sm  text-gray-900">
                            {data.strAmount || "-"}
                          </div>
                        </td>
                        <td className="  px-11 py-1.5  whitespace-nowrap">
                          <div className="text-sm  text-gray-900">
                            {data.strTranMode || "-"}
                          </div>
                        </td>
                        <td className="  px-11 py-1.5  whitespace-nowrap">
                          <div className="text-sm  text-gray-900">
                            {data.createdDate || "-"}
                          </div>
                        </td>
                        <td className="  px-11 py-1.5  whitespace-nowrap">
                          <div className="text-sm  text-gray-900">
                            {data.strRef || "-"}
                          </div>
                        </td>

                        <td className="  px-11 py-1.5 text-right whitespace-nowrap">
                          <div className="text-sm text-gray-900">
                            {data.strClosingBalance || "-"}
                          </div>
                        </td>
                        <td className="  px-11 py-1.5  whitespace-nowrap">
                          <div className="text-sm  text-gray-900">
                            {data.strCreated_by || "-"}
                          </div>
                        </td>
                        {/* <td className="px-12 py-1.5 whitespace-nowrap">
                          <div className="text-smclassName text-gray-900">
                            {data.strTotalCount || "-"}
                          </div>
                        </td> */}
                      </tr>
                    ))}
                </tbody>
              </table>
            </div>
          </>
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
    </AppLayout>
  );
}
