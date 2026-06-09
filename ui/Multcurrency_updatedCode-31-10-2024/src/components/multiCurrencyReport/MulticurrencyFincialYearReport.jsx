import React, { useState, useEffect, useRef, Fragment } from "react";
import AppLayout from "../../layout/AppLayout";
import amsApi from "../../api/amsApi";
import { utils, write } from "xlsx";
import { useReactToPrint } from "react-to-print";
import Switch from "@mui/joy/Switch";
import ReactLoading from "react-loading";
import Typography from "@mui/joy/Typography";
import { Dialog, Transition } from "@headlessui/react";
import { XMarkIcon } from "@heroicons/react/24/solid";
import AutoPagintation from "../../UI/AutoPagintation";
import {
  ArrowDownTrayIcon,
  MagnifyingGlassIcon,
} from "@heroicons/react/24/outline";
import CustomAlert from "../../layout/CustomAlert";
import AutoPagintationReport from "../reports/AutoPagintationReport";
const MulticurrencyFincialYearReport = () => {
  const [channeldata, setchanneldata] = useState([]);
  const [financialdata, setfinancialdata] = useState([]);
  let participantID = sessionStorage.getItem("Participantid");
 const [searchQuery, setSearchQuery] = useState("");
 const [itemPage, setItemPage] = useState(20);
 const [tcount, setTcount] = useState(1);
  const [alldata, setalldata] = useState([]);
  const [totalElement, setTotalElement] = useState("");
  const [type, setType] = useState([]);
  const [isLoadig, setisLoadig] = useState(false);
  const [error, setError] = useState("");
  const [accounttype, setAccountType] = useState("");
  const splitData = accounttype.split("-");
  let actype = splitData[0].trim();
  const [strAccountNumber, setstrAccountNumber] = useState("All");
  // Function to handle changes in the search input field
  const [showAlert, setShowAlert] = useState(false);
  const [alertType, setAlertType] = useState("");
  const [alertTitle, setAlertTitle] = useState("");
  const [alertMessage, setAlertMessage] = useState("");
  const [financialYear, setfinancialYear] = useState("");
  const [channelCode, setchannelCode] = useState("");

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

  const handleClick = () => {
    // setStartdate("");
    // setDuration("");
  };

  const [checked, setChecked] = useState(true);
  const TotalCount = alldata.map((t) => [t.strTotalCount])[0];

  useEffect(() => {
    handleSubmit();
  }, [tcount, itemPage]);

  const handleSubmit = async (event) => {
    if (
      strAccountNumber === "" ||
      actype === "" ||
      channelCode === "" ||
      financialYear === ""
    ) {
      setError(true);
    } else {
      setisLoadig(true);
      try {
        const response = await amsApi.post(
          `account-statement/getfinancialyearreport`,
          {
            accountNumber: strAccountNumber,
            accountType: actype,
            channelCode: channelCode,
            financialYear: financialYear,
            pageSize: itemPage,
            pageNumber: tcount,
          },
          {
            headers: {
              "Content-Type": "application/json",
            },
          }
        );
        if (response.data.code === "S0000") {
          setTotalElement(response.data.totalElementsSize);
          setalldata(response.data.multiCurrencyFinancialYearMastersList);

          setChecked(false);
          setisLoadig(false);
        } else {
          handleShowError(response.data.message);
          setChecked(true);
          setalldata([]);
          setisLoadig(false);
        }
      } catch (error) {
        handleShowError(
          "Sorry, the server is under mentaines. Please try again later"
        );
        setalldata([]);
        setisLoadig(false);
      }
    }
  };

    useEffect(() => {
      if (searchQuery === "" || searchQuery === " ") {
        handleSubmit();
        
      }
    }, [searchQuery]);

    const Search = async () => {
      try {
        const response = await amsApi.post(
          `account-statement/financialYearReportSearch/pagination/20/1`,
          {
            keyword: searchQuery,
          }
        );
        if (response.data.code === "S0000") {
          setalldata(response.data.financialYearSearch);
          setChecked(false);
          // handleShowSuccess(response.data.message);
        } else {
          handleShowError(response.data.message);
        }
      } catch (error) {
        // handleShowError(error.response.data.message);
      }
    };

  // Dropdown Channel code
  useEffect(() => {
    currencyCode();
  }, [accounttype]);

  const currencyCode = async () => {
    try {
      const response = await amsApi.post(
        `AcTypeLrsTcsMaster/getChanelListBasedOnAccountType`,
        {
          strAccountType: actype,
        },

        {
          headers: {
            "Content-Type": "application/json",
          },
        }
      );

      if (response.data.code === "S0000") {
        setchanneldata(response.data.acTypeLrsTcsMastersList);
      } else {
        // showError(response.data.message);
      }
    } catch (error) {
      // showError(error.response.data.message);
    }
  };
  // submit
  useEffect(() => {
    financialyearData();
  }, [accounttype, strAccountNumber, channelCode]);

  const financialyearData = async () => {
    try {
      const response = await amsApi.post(
        `multicurrencyfinancialyear/viewfinancialyears`,
        {
          accountType: actype,
          accountNumber: strAccountNumber,
          channelCode: channelCode,
        },

        {
          headers: {
            "Content-Type": "application/json",
          },
        }
      );

      if (response.data.code === "S0000") {
        setfinancialdata(response.data.multiCurrencyFinancialYearMastersList);
      } else {
        // showError(response.data.message);
      }
    } catch (error) {
      // showError(error.response.data.message);
    }
  };

  // Dropdown Account Type
  useEffect(() => {
    Accounttype();
  }, []);

  const Accounttype = async () => {
    try {
      const response = await amsApi.post(
        `accountType/getACCTypeListByParticiptWise`,

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
        // showError(response.data.message);
      }
    } catch (error) {
      // showError(error.response.data.message);
    }
  };

  // Download pdf and Excel
  const conponentPDF = useRef();
  const generatePDF = useReactToPrint({
    content: () => conponentPDF.current,
    documentTitle: "TableData",
  });
  const generateExcel = (data) => {
    const worksheet = utils.json_to_sheet(data);
    const workbook = { Sheets: { data: worksheet }, SheetNames: ["data"] };
    const excelBuffer = write(workbook, { bookType: "xlsx", type: "array" });
    const blob = new Blob([excelBuffer], {
      type: "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
    });
    const url = window.URL.createObjectURL(blob);
    const a = document.createElement("a");
    a.href = url;
    a.download = "Transaction Report.xlsx";
    a.click();
  };

  const [isOpen, setIsOpen] = useState(false);
  // Function to open the modal
  const openModal = () => {
    setIsOpen(true);
  };
  // Function to close the modal
  const closeModal = () => {
    setIsOpen(false);
  };

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

  // Sort the data based on the chosen column and order
  const sortedData = [...alldata].sort((a, b) => {
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

  return (
    <AppLayout>
      <div className="max-w-full mx-auto h-full">
        <div className="w-full shadow-md mt-2">
          <div className="sm:px-4 bg-blue-200 ">
            <div className=" flex  items-center justify-between">
              <p className="flex justify-start font-sans sm:text-xm text-black text-sm leading-normal">
                MultiCurrency Financial Year Reports
              </p>

              {/* </p> */}
              {isLoadig && (
                <div className="flex items-center justify-center my-10">
                  <ReactLoading
                    type="balls"
                    color="#0143E2"
                    height={"10%"}
                    width={"8%"}
                  />
                </div>
              )}

              <div>
                <button className="inline-flex   sm:mt-0 items-end justify-end px-2  focus:outline-none rounded text-xs font-medium">
                 
                  <Switch
                    checked={checked}
                    onChange={(event) => setChecked(event.target.checked)}
                    slotProps={{
                      track: {
                        children: (
                          <React.Fragment>
                            <Typography
                              component="span"
                              level="inherit"
                              sx={{ ml: "10px" }}
                            >
                              On
                            </Typography>
                            <Typography
                              component="span"
                              level="inherit"
                              sx={{ mr: "8px" }}
                            >
                              Off
                            </Typography>
                          </React.Fragment>
                        ),
                      },
                    }}
                    sx={{
                      "--Switch-thumbSize": "27px",
                      "--Switch-trackWidth": "64px",
                      "--Switch-trackHeight": "31px",
                    }}
                  />
                </button>
              </div>
            </div>
          </div>
        </div>

        {checked && (
          <div className=" md:col-span-2 lg:col-span-1  ">
            <div className="overflow-hidden shadow ">
              <div className=" px-2  sm:p-2 bg-white  ">
                <div className="grid gap-4  md:grid-cols-5 px-4">
                  <div>
                    <label
                      htmlFor="text"
                      className="block text-xs font-semibold text-gray-700 dark:text-white"
                    >
                      Account Type
                      <span className="text-red-600 px-2">*</span>
                    </label>
                    <select
                      id="accountType"
                      name="accountType"
                      value={accounttype}
                      onChange={(e) => setAccountType(e.target.value)}
                      className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0 "
                    >
                      <option value="">Select</option>
                      <option value="All">ALL</option>
                      {type.map((data) => (
                        <option value={data.strAccountType}>
                          {data.strAccountType}-{data.strDescription}
                        </option>
                      ))}
                    </select>
                  </div>

                  <div>
                    <label
                      htmlFor="duration"
                      className="block  text-xs font-semibold text-gray-700 dark:text-white"
                    >
                      Account No
                      <span className="text-red-600 px-2">*</span>
                    </label>
                    <input
                      type="text"
                      id="strAccountNumber"
                      value={strAccountNumber}
                      onChange={(e) => setstrAccountNumber(e.target.value)}
                      className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      placeholder="Enter Account No"
                      autoComplete="off"
                    />
                  </div>
                  <div>
                    <label
                      htmlFor="text"
                      className="block text-xs font-semibold text-gray-700 dark:text-white"
                    >
                      Channel Code
                      <span className="text-red-600 px-2">*</span>
                    </label>
                    <select
                      id="channelCode"
                      name="channelCode"
                      value={channelCode}
                      onChange={(e) => setchannelCode(e.target.value)}
                      className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0 "
                    >
                      <option value="">Select</option>
                      <option value="All">ALL</option>
                      {channeldata.map((data) => (
                        <option value={data.strChannelCode}>
                          {data.strChannelCode}
                        </option>
                      ))}
                    </select>
                  </div>
                  <div>
                    <label
                      htmlFor="text"
                      className="block text-xs font-semibold text-gray-700 dark:text-white"
                    >
                      Financial Year
                      <span className="text-red-600 px-2">*</span>
                    </label>
                    <select
                      id="financialYear"
                      name="financialYear"
                      value={financialYear}
                      onChange={(e) => setfinancialYear(e.target.value)}
                      className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                    >
                      <option value="">Select</option>
                      <option value="All">ALL</option>
                      {financialdata.map((data) => (
                        <option value={data.financialYear}>
                          {data.financialYear}
                        </option>
                      ))}
                    </select>
                  </div>

                  <div className=" mt-1">
                    <button
                      title="Click Search Button"
                      type="submit"
                      onClick={handleSubmit}
                      data-modal-toggle="defaultModal"
                      className="bg-blue-500 hover:bg-blue-700 focus:ring-2 focus:ring-blue-800   focus:ring-offset-2 text-white font-md py-2 px-4 border border-blue-700 rounded  shadow-lg inner"
                    >
                      <MagnifyingGlassIcon className="h-2.5 w-3" />
                    </button>
                    <button
                      title="Clear Data"
                      type="button"
                      onClick={handleClick}
                      className="inline-flex justify-center bg-rose-500 hover:bg-rose-700 focus:ring-2 focus:ring-rose-800 focus:ring-offset-2 py-2 px-4 mx-2 my-3 font-md  text-white  border  shadow-lg inner border-rose-700 rounded"
                    >
                      <XMarkIcon className="h-2.5 w-3" />
                    </button>
                    <button
                      title="Go Back"
                      onClick={() => openModal()}
                      className="inline-flex justify-center bg-green-500 hover:bg-green-700 focus:ring-2 focus:ring-green-800 focus:ring-offset-2 py-2 px-4 mx-2 my-3 font-md  text-white  border  shadow-lg inner border-green-700 rounded"
                    >
                      <ArrowDownTrayIcon className="h-2.5 w-3" />
                    </button>
                  </div>
                </div>
              </div>
            </div>
          </div>
        )}
       
        <AutoPagintationReport
          addPageperData={setItemPage}
          addCurrentPage={setTcount}
          searchData={setSearchQuery}
          pagePerData={itemPage}
          currentPage={tcount}
          tcount={alldata[0]?.strTotalCount}
          reCallApi={Search}
        />
        {/* <AutoPagintation
          addPageperData={setItemPage}
          addCurrentPage={setTcount}
          pagePerData={itemPage}
          currentPage={tcount}
          tcount={totalElement}
          reCallApi={handleSubmit}
        /> */}
        <div className="overflow-x-auto relative shadow-md ">
          <div className="table-wrp block max-h-[33rem] max-w-[27rem]">
            <div ref={conponentPDF} style={{ width: "100%" }}>
              <table className="w-full text-xs text-left text-black dark:text-blue-100">
                <thead className="text-xm border-b sticky top-0 text-gray-400 uppercase bg-gray-100 dark:text-white">
                  <th
                    scope="col"
                    className="py-3 px-9 whitespace-nowrap"
                    onClick={() => handleSort("date")}
                  >
                    Date{renderSortArrow("date")}
                  </th>
                  <th
                    scope="col"
                    className="py-3 px-9 whitespace-nowrap"
                    onClick={() => handleSort("time")}
                  >
                    Time{renderSortArrow("time")}
                  </th>
                  <th
                    scope="col"
                    className="py-3 px-9 whitespace-nowrap"
                    onClick={() => handleSort("accountType")}
                  >
                    account Type {renderSortArrow("accountType")}
                  </th>
                  <th
                    scope="col"
                    className="py-3 px-9 whitespace-nowrap"
                    onClick={() => handleSort("accountNumber")}
                  >
                    account Number {renderSortArrow("accountNumber")}
                  </th>
                  <th
                    scope="col"
                    className="py-3 px-9 whitespace-nowrap"
                    onClick={() => handleSort("custId")}
                  >
                    cust Id {renderSortArrow("custId")}
                  </th>

                  <th
                    scope="col"
                    className="py-3 px-9 whitespace-nowrap"
                    onClick={() => handleSort("financialYear")}
                  >
                    Financial Year {renderSortArrow("financialYear")}
                  </th>

                  <th
                    scope="col"
                    className="py-3 px-9 whitespace-nowrap "
                    onClick={() => handleSort("totalLrsConsumed")}
                  >
                    total Lrs Consumed {renderSortArrow("totalLrsConsumed")}
                  </th>
                  <th
                    scope="col"
                    className="py-3 px-9 whitespace-nowrap"
                    onClick={() => handleSort("totalTcsOn")}
                  >
                    total Tcs On {renderSortArrow("totalTcsOn")}
                  </th>

                  <th
                    scope="col"
                    className="py-3 px-9 whitespace-nowrap"
                    onClick={() => handleSort("lrsLimit")}
                  >
                    lrs Limit {renderSortArrow("lrsLimit")}
                  </th>
                  <th
                    scope="col"
                    className="py-3 px-9 whitespace-nowrap"
                    onClick={() => handleSort("channelCode")}
                  >
                    channel Code{renderSortArrow("channelCode")}
                  </th>

                  <th
                    scope="col"
                    className="py-3 px-9 whitespace-nowrap"
                    onClick={() => handleSort("availableLrsLimit")}
                  >
                    available Lrs Limit{renderSortArrow("availableLrsLimit")}
                  </th>
                  <th
                    scope="col"
                    className="py-3 px-9 whitespace-nowrap"
                    onClick={() => handleSort("totalExcessLoading")}
                  >
                    total Excess Loading{renderSortArrow("totalExcessLoading")}
                  </th>
                  <th
                    scope="col"
                    className="py-3 px-9 whitespace-nowrap"
                    onClick={() => handleSort("totalLoaded")}
                  >
                    total Loaded{renderSortArrow("totalLoaded")}
                  </th>
                </thead>
                <tbody>
                  {sortedData.length > 0 ? (
                    <>
                      {sortedData
                        .map(
                          ({
                            date,
                            time,
                            accountType,
                            accountNumber,
                            custId,
                            financialYear,
                            totalLrsConsumed,
                            totalTcsOn,
                            lrsLimit,
                            availableLrsLimit,
                            totalExcessLoading,
                            totalLoaded,
                            channelCode,
                          }) => (
                            <tr
                              // key={uuidv4()}
                              className="border-b dark:border-neutral-500"
                            >
                              <td className="whitespace-nowrap px-9 py-3">
                                <span className="text-xs font-medium text-gray-900">
                                  {date || "-"}
                                </span>
                              </td>
                              <td className="whitespace-nowrap px-9 py-3">
                                <span className="text-xs font-medium text-gray-900">
                                  {time || "-"}
                                </span>
                              </td>
                              <td className="whitespace-nowrap px-9 py-3">
                                <span className="text-xs font-medium text-gray-900">
                                  {accountType || "-"}
                                </span>
                              </td>
                              <td className="whitespace-nowrap px-9 py-3">
                                <span className="text-xs font-medium text-gray-900">
                                  {accountNumber || "-"}
                                </span>
                              </td>
                              <td className="whitespace-nowrap px-9 py-3">
                                <span className="text-xs font-medium text-gray-900">
                                  {custId || "-"}
                                </span>
                              </td>

                              <td className="whitespace-nowrap  text-center px-9 py-3">
                                <span className="text-xs font-medium text-gray-900">
                                  {financialYear || "-"}
                                </span>
                              </td>

                              <td className="whitespace-nowrap px-9 py-3 text-end">
                                <span className="text-xs font-medium text-gray-900">
                                  {totalLrsConsumed || "-"}
                                </span>
                              </td>
                              <td className="whitespace-nowrap px-9 py-3 text-end">
                                <span className="text-xs font-medium text-gray-900">
                                  {totalTcsOn || "-"}
                                </span>
                              </td>
                              <td className="whitespace-nowrap px-9 py-3 text-end">
                                <span className="text-xs font-medium text-gray-900">
                                  {lrsLimit || "-"}
                                </span>
                              </td>
                              <td className="whitespace-nowrap px-9 py-3 text-center">
                                <span className="text-xs font-medium text-gray-900">
                                  {channelCode || "-"}
                                </span>
                              </td>
                              <td className="whitespace-nowrap px-9 py-3 text-end">
                                <span className="text-xs font-medium text-gray-900">
                                  {availableLrsLimit || "-"}
                                </span>
                              </td>
                              <td className="whitespace-nowrap px-9 py-3 text-end">
                                <span className="text-xs font-medium text-gray-900">
                                  {totalExcessLoading || "-"}
                                </span>
                              </td>
                              <td className="whitespace-nowrap px-9 py-3 text-end">
                                <span className="text-xs font-medium text-gray-900">
                                  {totalLoaded || "-"}
                                </span>
                              </td>
                            </tr>
                          )
                        )}
                    </>
                  ) : (
                    <tr>
                      <td colSpan="10" className="px-9 py-1.5 text-center">
                        <span className="text-sm font-normal text-gray-500">
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
      {/* <MainPagination itemPageData={setItemPage} tCountData={setTcount} /> */}
      {/* </div> */}
      {/* Your existing code here */}

      {showAlert && (
        <CustomAlert
          title={alertTitle}
          message={alertMessage}
          type={alertType}
          onClose={handleCloseAlert}
        />
      )}
      {/* Delete model */}
      <Transition appear show={isOpen} as={Fragment}>
        <Dialog as="div" className="relative z-10" onClose={() => {}}>
          <Transition.Child
            as={Fragment}
            enter="ease-out duration-300"
            enterFrom="opacity-0"
            enterTo="opacity-100"
            leave="ease-in duration-200"
            leaveFrom="opacity-100"
            leaveTo="opacity-0"
          >
            <div className="fixed inset-0 bg-black bg-opacity-25" />
          </Transition.Child>

          <div className="fixed inset-0 overflow-y-auto">
            <div className="flex min-h-full items-center justify-center p-4 text-center">
              <Transition.Child
                as={Fragment}
                enter="ease-out duration-300"
                enterFrom="opacity-0 scale-95"
                enterTo="opacity-100 scale-100"
                leave="ease-in duration-200"
                leaveFrom="opacity-100 scale-100"
                leaveTo="opacity-0 scale-95"
              >
                <Dialog.Panel className="w-full max-w-md transform overflow-hidden rounded-2xl bg-white p-6 text-left align-middle shadow-xl transition-all">
                  <Dialog.Title
                    as="h3"
                    className="text-lg font-medium leading-6 text-gray-900"
                  >
                    Download
                  </Dialog.Title>
                  <div className="mt-2">
                    <p className="text-xs text-gray-500">
                      Are you sure you want to download file's ?
                    </p>
                  </div>
                  <div className=" px-6 py-1 sm:px-6 sm:flex sm:flex-row-reverse">
                    <button
                      type="button"
                      className="w-full inline-flex justify-center rounded-md border border-transparent shadow-sm px-4 py-2 bg-green-600 text-base font-medium text-white hover:bg-green-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-green-500 sm:ml-3 sm:w-auto sm:text-xs"
                      onClick={() => generatePDF(alldata)}
                    >
                      PDF
                    </button>
                    <button
                      type="button"
                      className="w-full inline-flex justify-center rounded-md border border-transparent shadow-sm px-4 py-2 bg-orange-600 text-base font-medium text-white hover:bg-orange-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-green-500 sm:ml-3 sm:w-auto sm:text-xs"
                      onClick={() => generateExcel(alldata)}
                    >
                      Excel
                    </button>
                    <button
                      type="button"
                      className="mt-3 w-full inline-flex justify-center rounded-md border border-gray-300 shadow-sm px-4 py-2 bg-white text-base font-medium text-gray-700 hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500 sm:mt-0 sm:ml-3 sm:w-auto sm:text-xs"
                      onClick={closeModal}
                    >
                      Cancel
                    </button>
                  </div>
                </Dialog.Panel>
              </Transition.Child>
            </div>
          </div>
        </Dialog>
      </Transition>

      {/*search Transaction  */}

      {/* Dilog box */}
    </AppLayout>
  );
};

export default MulticurrencyFincialYearReport;
